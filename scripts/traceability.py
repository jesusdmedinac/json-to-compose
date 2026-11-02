#!/usr/bin/env python3
"""
Traceability Tool for json-to-compose
Automates audit, synchronization, and next-scenario tracking across BDD feature files and PROGRESS.md trackers.

Usage:
  python3 scripts/traceability.py audit
  python3 scripts/traceability.py sync
  python3 scripts/traceability.py next [phase-name]
"""

import sys
import os
import glob
import re
from datetime import datetime

REPO_ROOT = os.path.abspath(os.path.join(os.path.dirname(__file__), ".."))
DOCS_PROJECTS = os.path.join(REPO_ROOT, "docs", "projects")
ROOT_PROGRESS = os.path.join(REPO_ROOT, "PROGRESS.md")

PHASE_ORDER = [
    ("phase-1-solidify-library", "library/"),
    ("phase-actions-system", "library/"),
    ("phase-demo-showcase", "composeApp/"),
    ("phase-3-expand-library", "library/"),
    ("phase-1.5-mvi-architecture", "composy/"),
    ("phase-2-editor-mvp", "composy/"),
    ("phase-4-semantics-testability", "library/"),
    ("phase-3-differentiators", "Multi-module"),
    ("phase-remote-engine-caching", "library/"),
]

def get_phase_stats():
    stats = []
    for phase_id, module in PHASE_ORDER:
        phase_dir = os.path.join(DOCS_PROJECTS, phase_id)
        progress_path = os.path.join(phase_dir, "PROGRESS.md")
        features_dir = os.path.join(phase_dir, "features")

        feature_files = sorted(glob.glob(os.path.join(features_dir, "*.feature"))) if os.path.exists(features_dir) else []
        scenarios_in_features = 0
        for f in feature_files:
            with open(f, "r", encoding="utf-8") as fp:
                scenarios_in_features += sum(1 for line in fp if line.strip().startswith("Scenario:"))

        checklist_total = 0
        checklist_completed = 0
        pending_scenarios = []

        if os.path.exists(progress_path):
            with open(progress_path, "r", encoding="utf-8") as fp:
                current_feature = None
                for line in fp:
                    l = line.strip()
                    if l.startswith("## Feature:"):
                        current_feature = l.replace("## Feature:", "").strip()
                    elif l.startswith("- [x]") or l.startswith("- [X]"):
                        checklist_total += 1
                        checklist_completed += 1
                    elif l.startswith("- [ ]"):
                        checklist_total += 1
                        scenario_name = l.replace("- [ ]", "").strip()
                        pending_scenarios.append((current_feature, scenario_name))

        pct = (checklist_completed / checklist_total * 100) if checklist_total > 0 else 0.0
        stats.append({
            "phase": phase_id,
            "module": module,
            "feature_count": len(feature_files),
            "feature_scenarios": scenarios_in_features,
            "checklist_total": checklist_total,
            "checklist_completed": checklist_completed,
            "progress_pct": pct,
            "pending_scenarios": pending_scenarios,
            "progress_path": progress_path,
            "features_dir": features_dir
        })
    return stats

def render_ascii_bar(completed, total, width=40):
    if total == 0:
        return "[" + "░" * width + "] 0%"
    pct = completed / total
    filled = int(round(pct * width))
    bar = "█" * filled + "░" * (width - filled)
    return f"[{bar}] {int(pct * 100)}%"

def cmd_audit():
    stats = get_phase_stats()
    print("=" * 96)
    print(" JSON-TO-COMPOSE: TRACEABILITY AUDIT")
    print("=" * 96)
    print(f"{'#':<3} {'Phase':<32} {'Module':<14} {'Features':<10} {'Progress':<16} {'Status':<12}")
    print("-" * 96)

    total_scenarios = 0
    total_completed = 0
    total_features = 0

    has_drift = False

    for idx, s in enumerate(stats, 1):
        total_scenarios += s["checklist_total"]
        total_completed += s["checklist_completed"]
        total_features += s["feature_count"]

        status = "✅ 100%" if s["checklist_completed"] == s["checklist_total"] and s["checklist_total"] > 0 else (
            f"🏗️ {s['progress_pct']:.0f}%" if s["checklist_completed"] > 0 else "📋 0%"
        )

        ratio = f"{s['checklist_completed']}/{s['checklist_total']}"
        print(f"{idx:<3} {s['phase']:<32} {s['module']:<14} {s['feature_count']:<10} {ratio:<16} {status:<12}")

    print("-" * 96)
    total_pct = (total_completed / total_scenarios * 100) if total_scenarios > 0 else 0
    print(f"TOTAL: {total_completed} / {total_scenarios} scenarios completed ({total_pct:.1f}%) across {total_features} features.")
    print(render_ascii_bar(total_completed, total_scenarios))
    print("=" * 96)

def cmd_sync():
    stats = get_phase_stats()
    total_scenarios = sum(s["checklist_total"] for s in stats)
    total_completed = sum(s["checklist_completed"] for s in stats)
    total_pct = (total_completed / total_scenarios * 100) if total_scenarios > 0 else 0

    if not os.path.exists(ROOT_PROGRESS):
        print(f"Error: {ROOT_PROGRESS} not found.")
        sys.exit(1)

    with open(ROOT_PROGRESS, "r", encoding="utf-8") as fp:
        content = fp.read()

    # Generate new table
    table_lines = [
        "| #   | Phase                                                             | Module        | Scenarios | Completed | Progress |",
        "| --- | ----------------------------------------------------------------- | ------------- | --------- | --------- | -------- |"
    ]

    for idx, s in enumerate(stats, 1):
        pct_str = f"**{int(s['progress_pct'])}%**" if s['checklist_completed'] == s['checklist_total'] else f"**{s['progress_pct']:.0f}%**"
        link_name = s['phase']
        row = f"| {idx:<3} | [{link_name}](#{s['phase']}) | `{s['module']}` | {s['checklist_total']:<9} | {s['checklist_completed']:<9} | {pct_str:<8} |"
        table_lines.append(row)

    total_pct_str = f"**{int(total_pct)}%**"
    table_lines.append(f"|     | **TOTAL**                                                         |               | {total_scenarios:<9} | {total_completed:<9} | {total_pct_str:<8} |")
    new_table = "\n".join(table_lines)

    # Replace table in content
    table_pattern = re.compile(
        r"\| #\s+\| Phase.*?\n\| --- \| ---.*?\n(?:\|.*?\|\n)+",
        re.MULTILINE
    )
    if not table_pattern.search(content):
        print("Warning: Could not match progress table in root PROGRESS.md")
    else:
        content = table_pattern.sub(new_table + "\n", content, count=1)

    # Replace progress bar
    bar_pattern = re.compile(r"Progress: \[.*?\] \d+%", re.MULTILINE)
    new_bar = f"Progress: {render_ascii_bar(total_completed, total_scenarios)}"
    content = bar_pattern.sub(new_bar, content, count=1)

    # Update date
    today_str = datetime.now().strftime("%Y-%m-%d")
    content = re.sub(r"> Last updated: \d{4}-\d{2}-\d{2}", f"> Last updated: {today_str}", content, count=1)

    with open(ROOT_PROGRESS, "w", encoding="utf-8") as fp:
        fp.write(content)

    print(f"✅ Synchronized {ROOT_PROGRESS} successfully.")
    print(f"   Total Scenarios: {total_completed}/{total_scenarios} ({total_pct:.1f}%)")
    print(f"   {new_bar}")

def cmd_next(target_phase=None):
    stats = get_phase_stats()
    candidates = [s for s in stats if s["pending_scenarios"]]
    if target_phase:
        candidates = [s for s in candidates if s["phase"] == target_phase]

    if not candidates:
        print("🎉 No pending scenarios found! All phases complete.")
        return

    active_phase = candidates[0]
    feature_name, scenario_line = active_phase["pending_scenarios"][0]

    print("=" * 80)
    print(f" NEXT ACTIONABLE SCENARIO")
    print("=" * 80)
    print(f"Phase:      {active_phase['phase']} ({active_phase['module']})")
    print(f"Feature:    {feature_name}")
    print(f"Scenario:   {scenario_line}")
    print(f"Tracker:    {active_phase['progress_path']}")
    print("-" * 80)
    print(f"Suggested Commit Template:")
    slug = re.sub(r"[^a-zA-Z0-9_-]+", "_", scenario_line.lower()).strip("_")
    print(f"  git commit -m \"feat: {scenario_line.replace('Scenario: ', '')} [{slug}]\"")
    print("=" * 80)

if __name__ == "__main__":
    if len(sys.argv) < 2:
        cmd_audit()
    else:
        action = sys.argv[1].lower()
        if action == "audit":
            cmd_audit()
        elif action == "sync":
            cmd_sync()
        elif action == "next":
            phase = sys.argv[2] if len(sys.argv) > 2 else None
            cmd_next(phase)
        else:
            print("Unknown command. Options: audit | sync | next [phase]")

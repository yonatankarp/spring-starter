#!/usr/bin/env python3
"""Bootstrap a fresh project from this template.

Replaces template placeholders (`kotlin-spring-boot-template` slug,
`karp.kotlin.spring.boot.template` package, `8080` port), renames the
Gradle subprojects and Kotlin source directories to match the new
package, cleans up template-only README sections, and self-destructs.
"""

from __future__ import annotations

import re
import subprocess
import sys
from pathlib import Path

REPO_ROOT = Path(__file__).resolve().parent.parent

PLACEHOLDER_SLUG = "kotlin-spring-boot-template"
PLACEHOLDER_PACKAGE = "karp.kotlin.spring.boot.template"

MODULE_SUFFIXES = ["domain", "application", "adapters"]

PORT_FILES = [
    "README.md",
    "Dockerfile",
    f"{PLACEHOLDER_SLUG}-adapters/src/main/resources/application.yml",
]

SLUG_FILES = [
    "build.gradle.kts",
    "settings.gradle.kts",
    "README.md",
    "docker-compose.yml",
    "Dockerfile",
    f"buildSrc/src/main/kotlin/{PLACEHOLDER_SLUG}.java-conventions.gradle.kts",
    f"buildSrc/src/main/kotlin/{PLACEHOLDER_SLUG}.publishing-conventions.gradle.kts",
    f"buildSrc/src/main/kotlin/{PLACEHOLDER_SLUG}.spotless.gradle.kts",
    ".github/workflows/build.yml",
    *[f"{PLACEHOLDER_SLUG}-{s}/build.gradle.kts" for s in MODULE_SUFFIXES],
    f"{PLACEHOLDER_SLUG}-adapters/src/main/resources/application.yml",
]

README_SECTIONS_TO_DROP = ["Purpose", "What's inside", "Setup"]


def prompt(message: str, default: str | None = None) -> str:
    suffix = f" (press Enter for default {default})" if default else ""
    response = input(f"{message}{suffix}: ").strip()
    return response or (default or "")


def replace_in_file(path: Path, find: str, replace: str) -> None:
    if not path.exists():
        return
    text = path.read_text(encoding="utf-8")
    if find in text:
        path.write_text(text.replace(find, replace), encoding="utf-8")


def git_mv(src: Path, dst: Path) -> None:
    subprocess.run(["git", "mv", str(src), str(dst)], check=True, cwd=REPO_ROOT)


def gather_inputs() -> tuple[str, str, str]:
    port = prompt("Port number for new app", default="8080")
    component_name = prompt("Replace application name with")
    package = prompt(f"Replace `{PLACEHOLDER_PACKAGE}` last segment with")

    if not port.isdigit() or not (1 <= int(port) <= 65535):
        print("Port must be a number between 1 and 65535.", file=sys.stderr)
        sys.exit(1)
    if not re.fullmatch(r"[a-z0-9-]+", component_name):
        print("Application name must match [a-z0-9-]+ (lowercase kebab-case).", file=sys.stderr)
        sys.exit(1)
    if not re.fullmatch(r"[a-z][a-z0-9_]*", package):
        print("Package segment must match [a-z][a-z0-9_]* (single Java/Kotlin identifier).", file=sys.stderr)
        sys.exit(1)
    return port, component_name, package


def replace_port(port: str) -> None:
    for relative in PORT_FILES:
        replace_in_file(REPO_ROOT / relative, "8080", port)


def replace_slug(component_name: str) -> None:
    for relative in SLUG_FILES:
        replace_in_file(REPO_ROOT / relative, PLACEHOLDER_SLUG, component_name)


def replace_package(new_package: str) -> None:
    replace_in_file(REPO_ROOT / "build.gradle.kts", PLACEHOLDER_PACKAGE, new_package)
    for suffix in MODULE_SUFFIXES:
        module = REPO_ROOT / f"{PLACEHOLDER_SLUG}-{suffix}"
        replace_in_file(module / "build.gradle.kts", PLACEHOLDER_PACKAGE, new_package)
        src_dir = module / "src"
        if not src_dir.exists():
            continue
        for source in src_dir.rglob("*"):
            if source.is_file():
                replace_in_file(source, PLACEHOLDER_PACKAGE, new_package)


def rename_kotlin_packages(package: str) -> None:
    for suffix in MODULE_SUFFIXES:
        module = REPO_ROOT / f"{PLACEHOLDER_SLUG}-{suffix}"
        for layer in ("main", "test", "testFixtures"):
            old_pkg = module / "src" / layer / "kotlin" / "com" / "yonatankarp" / "kotlin" / "spring" / "boot" / "template"
            new_pkg = module / "src" / layer / "kotlin" / "com" / "yonatankarp" / package
            if old_pkg.exists():
                git_mv(old_pkg, new_pkg)


def rename_module_directories(component_name: str) -> None:
    for suffix in MODULE_SUFFIXES:
        old = REPO_ROOT / f"{PLACEHOLDER_SLUG}-{suffix}"
        new = REPO_ROOT / f"{component_name}-{suffix}"
        if old.exists():
            git_mv(old, new)


def rename_buildsrc_files(component_name: str) -> None:
    for old in REPO_ROOT.rglob(f"{PLACEHOLDER_SLUG}*"):
        path = old.as_posix()
        if "buildSrc/build" in path or "/.gradle/" in path:
            continue
        new = old.parent / old.name.replace(PLACEHOLDER_SLUG, component_name)
        if old != new:
            git_mv(old, new)


def clean_readme(component_name: str) -> None:
    readme = REPO_ROOT / "README.md"
    text = readme.read_text(encoding="utf-8")
    for section in README_SECTIONS_TO_DROP:
        text = re.sub(rf"^## {re.escape(section)}.*?(?=^## )", "", text, flags=re.DOTALL | re.MULTILINE)
    text = re.sub(r"^# .+$", f"# {component_name}", text, count=1, flags=re.MULTILINE)
    readme.write_text(text, encoding="utf-8")


def self_destruct() -> None:
    print("Self-destruct in 3... 2... 1...")
    script = Path(__file__).resolve()
    script.unlink(missing_ok=True)
    try:
        script.parent.rmdir()
    except OSError:
        pass


def main() -> int:
    port, component_name, package = gather_inputs()
    new_package = f"karp.{package}"

    replace_port(port)
    replace_slug(component_name)
    replace_package(new_package)
    rename_kotlin_packages(package)
    rename_module_directories(component_name)
    rename_buildsrc_files(component_name)
    clean_readme(component_name)
    self_destruct()
    return 0


if __name__ == "__main__":
    sys.exit(main())

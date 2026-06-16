from flask import Flask, request, jsonify

app = Flask(__name__)

def classify_vulnerability(vuln_type):
    high_risk = ["sql injection", "command injection", "hardcoded"]
    medium_risk = ["log injection", "xss"]
    vuln_lower = vuln_type.lower()
    for k in high_risk:
        if k in vuln_lower:
            return "P0", "高危"
    for k in medium_risk:
        if k in vuln_lower:
            return "P1", "中危"
    return "P2", "低危"

@app.route("/api/analyze", methods=["POST"])
def analyze():
    data = request.get_json()
    if not data or "vulnerabilities" not in data:
        return jsonify({"error": "缺少漏洞数据"}), 400
    results = []
    for vuln in data.get("vulnerabilities", []):
        level, level_name = classify_vulnerability(vuln.get("type", ""))
        results.append({
            "type": vuln.get("type"),
            "location": vuln.get("location", "未知"),
            "level": level,
            "level_name": level_name,
            "fix_suggestion": "建议人工审查"
        })
    return jsonify({"status": "ok", "results": results})

if __name__ == "__main__":
    app.run(port=8080)

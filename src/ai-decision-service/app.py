import os
from openai import OpenAI
from flask import Flask, request, jsonify

app = Flask(__name__)

# 初始化 DeepSeek 客户端
client = OpenAI(
    api_key=os.getenv("DEEPSEEK_API_KEY", "你的API_KEY"),
    base_url="https://api.deepseek.com/v1"
)

def ai_analyze_vulnerability(vuln_type, code_snippet):
    """调用 DeepSeek 进行漏洞分析和分级"""
    response = client.chat.completions.create(
        model="deepseek-chat",
        messages=[
            {"role": "system", "content": """
你是安全专家。对漏洞做分级和修复建议：
- P0（高危）：SQL注入、命令注入、硬编码密钥
- P1（中危）：日志注入、XSS
- P2（低危）：其他
输出格式：等级|等级名称|修复建议
            """},
            {"role": "user", "content": f"漏洞类型：{vuln_type}\n代码片段：{code_snippet}"}
        ],
        temperature=0.3,
        max_tokens=200
    )
    
    result = response.choices[0].message.content
    # 解析返回的 "P0|高危|使用预编译语句"
    parts = result.split('|')
    return {
        "level": parts[0] if len(parts) > 0 else "P2",
        "level_name": parts[1] if len(parts) > 1 else "低危",
        "fix_suggestion": parts[2] if len(parts) > 2 else "建议人工审查"
    }

@app.route("/api/analyze", methods=["POST"])
def analyze():
    data = request.get_json()
    if not data or "vulnerabilities" not in data:
        return jsonify({"error": "缺少漏洞数据"}), 400
    
    results = []
    for vuln in data.get("vulnerabilities", []):
        ai_result = ai_analyze_vulnerability(
            vuln.get("type", ""),
            vuln.get("code", "")
        )
        results.append({
            "type": vuln.get("type"),
            "location": vuln.get("location", "未知"),
            **ai_result
        })
    
    return jsonify({"status": "ok", "results": results})

if __name__ == "__main__":
    app.run(port=8080)

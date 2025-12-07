from flask import Flask, request, jsonify

app = Flask(__name__)

@app.route('/calcular', methods=['POST'])
def calcular():
    data = request.get_json()
    valor_base = data.get("valor_base", 0)
    
    # Simulación de cálculos
    iva = valor_base * 0.19
    retencion = valor_base * 0.12

    return jsonify({"iva": iva, "retencion": retencion})

if __name__ == '__main__':
    app.run(port=5000)

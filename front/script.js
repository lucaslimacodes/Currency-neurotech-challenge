async function buscarDados() {
    const startDate = document.getElementById('startDate').value;
    const endDate = document.getElementById('endDate').value;

    if (!startDate || !endDate) {
        alert("Por favor, selecione ambas as datas.");
        return;
    }

    try {
        const response = await fetch(`http://127.0.0.1:8080/interval?startDate=${startDate}&endDate=${endDate}`);
        const data = await response.json();

        plotarGrafico(data);
    } catch (error) {
        console.error("Erro ao buscar dados:", error);
        alert("Erro ao carregar os dados.");
    }
}

async function obterCotacaoAtual() {
    try {
        const response = await fetch('http://127.0.0.1:8080/latest');
        const data = await response.json();

        if (data) {
            return {
                compra: data.cotacaoCompra,
                venda: data.cotacaoVenda
            };
        } else {
            throw new Error("Erro ao obter a cotação atual.");
        }
    } catch (error) {
        console.error("Erro ao buscar a cotação atual:", error);
        alert("Erro ao carregar a cotação atual.");
    }
}

async function converterValor() {
    const valorReais = parseFloat(document.getElementById('valorReais').value);

    if (isNaN(valorReais) || valorReais <= 0) {
        alert("Por favor, insira um valor válido.");
        return;
    }

    try {
        const { compra, venda } = await obterCotacaoAtual();

        const valorCompra = valorReais * compra;
        const valorVenda = valorReais * venda; 

        document.getElementById('resultadoCompra').textContent = `Valor em Cotação de Compra: US$ ${valorCompra}`;
        document.getElementById('resultadoVenda').textContent = `Valor em Cotação de Venda: US$ ${valorVenda}`;
    } catch (error) {
        console.error("Erro ao converter valor:", error);
        alert("Erro ao realizar a conversão.");
    }
}

function plotarGrafico(dados) {
    const ctx = document.getElementById('grafico').getContext('2d');
    
    const labels = dados.map(item => {
        const date = new Date(item.dataCambio);
        return date.toISOString().split('T')[0];
    });
    console.log(labels);
    const compra = dados.map(item => item.cotacaoCompra);
    console.log(dados);
    const venda = dados.map(item => item.cotacaoVenda);

    if (window.meuGrafico) {
        window.meuGrafico.destroy();
    }

    window.meuGrafico = new Chart(ctx, {
        type: 'line',
        data: {
            labels,
            datasets: [
                {
                    label: 'Cotação Compra',
                    data: compra,
                    borderColor: 'blue',
                    fill: false
                },
                {
                    label: 'Cotação Venda',
                    data: venda,
                    borderColor: 'red',
                    fill: false
                }
            ]
        },
        options: {
            responsive: true,
            scales: {
                x: {
                    title: { display: true, text: 'Data' }
                },
                y: {
                    title: { display: true, text: 'Valor' }
                }
            }
        }
    });
}

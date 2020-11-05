import React from 'react';
import { Typography } from '@material-ui/core';
import { Bar } from 'react-chartjs-2';

const TotalDebtChart = ({ loanList }) => {

  const labels = loanList.flatMap(l => l.loanPrincipals)
    .map(lp => lp.date)
    .filter((date, index, dates) => dates.indexOf(date) === index)
    .sort((a, b) => b.localeCompare(a));

  const loanPrincipalsMap: Map<string, number> = new Map<>()
  loanList.flatMap(l => l.loanPrincipals).forEach(lp => {
    if (loanPrincipalsMap.has(lp.date)) {
      loanPrincipalsMap.set(lp.date, loanPrincipalsMap.get(lp.date) + lp.amount)
    } else {
      loanPrincipalsMap.set(lp.date, lp.amount)
    }
  })

  const totalData = Array.from(loanPrincipalsMap)
    .map(value => ({
      date: value[0],
      amount: value[1]
    }))
    .sort((a, b) => b.date.localeCompare(a.date))
    .map(lp => lp.amount);

  const chartData = {
    labels: labels,
    datasets: [
      {
        label: 'Total Debt',
        fill: false,
        backgroundColor: `rgb(68, 157, 209)`,
        borderColor: `rgb(68, 157, 209, 0.2)`,
        data: totalData
      }
    ]
  };

  const options = {
    legend: {
      display: false
    },
    scales: {
      yAxes: [
        {
          ticks: {
            beginAtZero: false,
          },
        },
      ],
    },
  };

  return (
    <div style={{ marginBottom: '5rem', marginTop: '5rem' }}>
      <Typography variant="h4" align="center">Total Debt</Typography>
      <Bar data={chartData} options={options} />
    </div>
  );
}

export default TotalDebtChart;

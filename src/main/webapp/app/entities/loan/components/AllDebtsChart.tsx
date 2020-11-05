import React from 'react';
import { Line } from 'react-chartjs-2';

const AllDebtsChart = ({ loanList }) => {

  const labels = loanList.flatMap(l => l.loanPrincipals)
    .map(lp => lp.date)
    .filter((date, index, dates) => dates.indexOf(date) === index)
    .sort((a, b) => b.localeCompare(a));

  const datasets = loanList.slice()
    .map( loan => {
      const red = Math.round(Math.random()*255);
      const blue = Math.round(Math.random()*255);
      const green = Math.round(Math.random()*255);

      return ({
        label: loan.name,
        fill: false,
        backgroundColor: `rgb(${red}, ${blue}, ${green})`,
        borderColor: `rgba(${red}, ${blue}, ${green}, 0.2)`,
        data: loan.loanPrincipals
          .slice()
          .sort((a, b) => b.date.localeCompare(a.date))
          .map(lp => lp.amount)
      })});

  const chartData = {
    labels: labels,
    datasets: datasets,
  };

  const options = {
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
      <Line data={chartData} options={options} />
    </div>
  )
}

export default AllDebtsChart;

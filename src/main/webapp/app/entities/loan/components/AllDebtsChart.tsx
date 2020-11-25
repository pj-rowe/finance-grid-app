import React, { useState } from 'react';
import { Line } from 'react-chartjs-2';

import DebtLabelButtons from 'app/entities/loan/components/DebtLabelButtons';
import DebtConfiguration from 'app/entities/loan/components/DebtConfiguration';

const inititalCurrentDebt: { id?: string, name: string, loanPrincipals: { date: string, amount: number }[]  } | null = null;

const AllDebtsChart = ({ loanList }) => {

  const [currentDebt, setCurrentDebt] = useState(inititalCurrentDebt)

  const handleClickOpen = (currentDebt) => {
    setCurrentDebt(currentDebt);
  };

  const handleClose = () => {
    setCurrentDebt(inititalCurrentDebt);
  };

  const labels = loanList.flatMap(l => l.loanPrincipals)
    .map(lp => lp.date)
    .filter((date, index, dates) => dates.indexOf(date) === index)
    .sort((a, b) => b.localeCompare(a));

  const datasets = loanList.slice()
    .map( loan => {
      const red = Math.round(Math.random()*192);
      const blue = Math.round(Math.random()*192);
      const green = Math.round(Math.random()*192);

      return ({
        label: loan.name,
        loanId: loan.id,
        fill: false,
        backgroundColor: `rgb(${red}, ${blue}, ${green})`,
        borderColor: `rgba(${red}, ${blue}, ${green}, 0.5)`,
        loanPrincipals: loan.loanPrincipals,
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
      <DebtLabelButtons datasets={ datasets } openConfig={ handleClickOpen } />
      <Line data={ chartData } options={ options } />
      <DebtConfiguration currentDebt={ currentDebt } handleClose={ handleClose } />
    </div>
  )
}

export default AllDebtsChart;

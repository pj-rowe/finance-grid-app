import React from 'react';
import { Line } from 'react-chartjs-2';
import { Grid, Button, Typography } from '@material-ui/core';
import { GoPrimitiveDot as DotIcon } from 'react-icons/all';


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
        borderColor: `rgba(${red}, ${blue}, ${green}, 0.5)`,
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
      <Grid container justify="center" spacing={1}>
        {
          datasets.map(d => (
            <Grid item>
              <Button variant="outlined" color="secondary" size="small" startIcon={ <DotIcon />} style={{ color: d.backgroundColor, borderColor: d.borderColor }}>
                <Typography variant="body2">{ d.label }</Typography>
              </Button>
            </Grid>
          ))
        }
      </Grid>
      <Line data={chartData} options={options} />
    </div>
  )
}

export default AllDebtsChart;

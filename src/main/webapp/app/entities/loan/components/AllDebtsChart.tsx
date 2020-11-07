import React from 'react';
import { Line } from 'react-chartjs-2';
import { Grid, Button, Typography, makeStyles, Theme, createStyles } from '@material-ui/core';
import { GoPrimitiveDot as DotIcon } from 'react-icons/all';

const debtLabelBtnStyles = makeStyles((theme: Theme) =>
  createStyles({
    debtLabelBtn: {
      backgroundColor: 'inherit',
      color: (props: { customColor: string }) => {
        return props.customColor
      },
      borderColor: (props: { borderColor: string }) => {
        return props.borderColor
      },
      '&:hover': {
        color: '#fff',
        backgroundColor: (props: { customColor: string }) => {
          return props.customColor
        },
        borderColor: (props: { borderColor: string }) => {
          return props.borderColor
        },
      },
      '&:active': {
        borderColor: (props: { borderColor: string }) => {
          return props.borderColor
        },
      },
      '&:focus': {
        outline: 'none'
      }
    }
  })
)

const AllDebtsChart = ({ loanList }) => {

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

  const styles = datasets.map(d => debtLabelBtnStyles({ customColor: d.backgroundColor, borderColor: d.borderColor }))

  return (
    <div style={{ marginBottom: '5rem', marginTop: '5rem' }}>
      <Grid container justify="center" spacing={1} style={{ marginBottom: '1rem' }}>
        {
          datasets.map((d, i) => (
            <Grid item>
              <Button className={ styles[i].debtLabelBtn } variant="outlined" color="secondary" size="small" startIcon={ <DotIcon />}>
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

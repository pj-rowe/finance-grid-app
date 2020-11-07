import React, { useState, FunctionComponent } from 'react';
import { Line } from 'react-chartjs-2';
import {
  List,
  ListItem,
  ListItemText,
  Button,
  Dialog,
  DialogTitle,
  DialogContent,
  TextField,
  DialogActions,
  ListItemAvatar,
  Avatar,
  Typography
} from '@material-ui/core';
import { MdPayment as PaymentIcon, AiOutlineAppstoreAdd as AddIcon, AiOutlineSave as SaveIcon } from 'react-icons/all';

import DebtLabelButtons from 'app/entities/loan/components/DebtLabelButtons';

const inititalCurrentDebt: { id?: string, name: string, loanPrincipals: { date: string, amount: number }[]  } | null = null

const AllDebtsChart = ({ loanList }) => {

  const [configOpen, setConfigOpen] = useState(false);
  const [currentDebt, setCurrentDebt] = useState(inititalCurrentDebt)

  const handleClickOpen = (currentDebt) => {
    setCurrentDebt(currentDebt);
    setConfigOpen(true);
  };

  const handleClose = () => {
    setConfigOpen(false);
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
      <DebtLabelButtons datasets={datasets} openConfig={handleClickOpen} />
      <Line data={chartData} options={options} />
      { currentDebt &&
        <Dialog open={configOpen} onClose={handleClose} aria-labelledby="form-dialog-title" fullWidth>
          <DialogTitle id="form-dialog-title">Debt Configuration</DialogTitle>
          <DialogContent dividers={true}>
            <TextField
              id="debtName"
              label="Debt name"
              type="text"
              variant="outlined"
              value={currentDebt.name}
              fullWidth
              style={{ marginTop: '2rem' }}
            />
            <>
              <div style={{ marginTop: '2rem', display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                <Typography variant="button" style={{ fontSize: '1rem' }}>Principals</Typography>
                <Button variant="contained" color="primary" startIcon={ <AddIcon /> }>Add Principal</Button>
              </div>
              <List style={{ marginTop: '0.5rem'}}>
                {
                  currentDebt.loanPrincipals.sort((a, b) => b.date.localeCompare(a.date)).map(lp => (
                    <ListItem key={lp.date} disableGutters>
                      <ListItemAvatar>
                        <Avatar style={{ backgroundColor: '#00A878' }}>
                          <PaymentIcon color="black" />
                        </Avatar>
                      </ListItemAvatar>
                      <ListItemText
                        primary={
                          <TextField
                            label="Date"
                            type="date"
                            value={lp.date}
                          />
                        }
                      />
                      <ListItemText
                        primary={
                          <TextField
                            label="Amount"
                            type="number"
                            value={lp.amount}
                          />
                        }
                      />
                    </ListItem>
                  ))
                }
              </List>
            </>
          </DialogContent>
          <DialogActions style={{ padding: '1rem' }}>
            <Button onClick={handleClose} color="default">
              Cancel
            </Button>
            <Button variant="contained" onClick={handleClose} color="primary" startIcon={ <SaveIcon /> }>
              Save Changes
            </Button>
          </DialogActions>
        </Dialog>
      }
    </div>
  )
}

export default AllDebtsChart;

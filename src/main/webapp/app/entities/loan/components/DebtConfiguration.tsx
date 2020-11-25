import React, { makeStyles, createStyles, Theme } from 'react';

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

const useStyles = makeStyles((theme: Theme) =>
  createStyles({
    
  })
);

const DebtConfiguration = ({ currentDebt, handleClose }) => {
  return currentDebt && (
    <Dialog open= onClose={ handleClose } aria-labelledby="form-dialog-title" fullWidth>
      <DialogTitle id="form-dialog-title">Debt Configuration</DialogTitle>
      <DialogContent dividers={true}>
        <TextField
          id="debtName"
          label="Debt name"
          type="text"
          variant="outlined"
          value={ currentDebt.name }
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
                  <ListItemText primary={ <TextField label="Amount" type="number" value={lp.amount} /> } />
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
  );
}

export default DebtConfiguration;

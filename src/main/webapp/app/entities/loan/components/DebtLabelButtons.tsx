import React from 'react';
import {
  Grid,
  Button,
  Typography,
  makeStyles,
  Theme,
  createStyles,
} from '@material-ui/core';
import { BiCheckboxSquare as DotIcon } from 'react-icons/all';

const debtLabelBtnStyles = makeStyles((theme: Theme) =>
  createStyles({
    debtLabelBtn: (props: { customColor: string, borderColor: string }) => ({
      backgroundColor: 'inherit',
      color: props.customColor,
      borderColor: props.borderColor,
      '&:hover': {
        color: '#fff',
        backgroundColor: props.customColor,
        borderColor: props.borderColor
      },
      '&:active': {
        borderColor: props.borderColor,
      },
      '&:focus': {
        outline: 'none'
      }
    })
  })
)

const DebtLabelButtons = ({ datasets, openConfig }) => {

  const styles = datasets.map(d => debtLabelBtnStyles({ customColor: d.backgroundColor, borderColor: d.borderColor }))

  return (
    <Grid container justify="center" spacing={1} style={{ marginBottom: '1rem' }}>
      {
        datasets.map((d, i) => (
          <Grid item>
            <Button
              className={ styles[i].debtLabelBtn }
              variant="outlined"
              color="secondary"
              size="small"
              startIcon={ <DotIcon />}
              onClick={
                () => openConfig({
                  id: d.loanId,
                  name: d.label,
                  loanPrincipals: d.loanPrincipals
                })
              }
            >
              <Typography variant="body2">{ d.label }</Typography>
            </Button>
          </Grid>
        ))
      }
    </Grid>
  )
};

export default DebtLabelButtons;

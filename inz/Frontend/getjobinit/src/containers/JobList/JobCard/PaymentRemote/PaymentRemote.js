import React from 'react';
import Grid from '@material-ui/core/Grid';
import Chip from '@material-ui/core/Chip';
import LocalAtmIcon from '@material-ui/icons/LocalAtm';
import TelegramIcon from '@material-ui/icons/Telegram';

const paymentRemote = (props) => {
 
  let payment = props.salaryMin + " - " + props.salaryMax;

  let remote = null;
  if(props.remote) {
    remote = (
      <Chip
        label={remote}
        deleteIcon={<TelegramIcon />}
      />
    );
  }

  return (
  <Grid item>
    <Grid container direction="column">
      <Grid item>
        <Chip
          label={payment}
          icon={<LocalAtmIcon />}
          color='secondary'
        />
      </Grid>
      <Grid item>
        {remote}
      </Grid>
    </Grid>
  </Grid>
  )
}

export default paymentRemote;
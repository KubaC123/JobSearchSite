import React from 'react';
import Grid from '@material-ui/core/Grid';
import Typography from '@material-ui/core/Typography';
import BusinessIcon from '@material-ui/icons/Business';
import LocationOnIcon from '@material-ui/icons/LocationOn';

const briefInfo = (props) => (
  <Grid item>
    <Typography variant="h6">
      <strong>{props.title}</strong>
    </Typography>
    <Grid container direction="row" alignItems="center" spacing={1}>
      <Grid item>
        <BusinessIcon />
      </Grid>
      <Grid item>
        <Typography>
          <strong>{props.company}</strong>
        </Typography>
      </Grid>
    </Grid>
    <Grid container direction="row" alignItems="center" spacing={1}>
      <Grid item>
        <LocationOnIcon />
      </Grid>
      <Grid item>
        <Typography>
          <strong>{props.location}</strong>
        </Typography>
      </Grid>
    </Grid>
  </Grid>
);

export default briefInfo;


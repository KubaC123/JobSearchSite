import React from 'react';
import Box from '@material-ui/core/Box';
import CardMedia from '@material-ui/core/CardMedia';
import Grid from '@material-ui/core/Grid';

const companyLogo = (props) => (
  <Grid item>
    <Box maxWidth="100px">
      <CardMedia
        component="img"
        width="100%"
        height="auto"
        src={props.imageUrl}
      />
    </Box>
  </Grid>
);

export default companyLogo;
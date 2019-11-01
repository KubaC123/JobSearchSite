import React from 'react';
import Paper from '@material-ui/core/Paper';
import Typography from '@material-ui/core/Typography';
import Divider from '@material-ui/core/Divider';
import Grid from '@material-ui/core/Grid';
import Box from '@material-ui/core/Box';
import FacebookIcon from '@material-ui/icons/Facebook';
import InstagramIcon from '@material-ui/icons/Instagram';
import TwitterIcon from '@material-ui/icons/Twitter';
import LinkedInIcon from '@material-ui/icons/LinkedIn';

const companyData = (props) => {

  let style = {
    paper: {
      width: "250px",
      marginLeft: "auto", 
      marginRight: "auto"
    },
    typography: {
      paddingLeft: 10,
      paddingTop: 5,
      paddingBottom: 5
    }
  }

  return (
    <Paper style={style.paper}>
      <Typography variant="h6" style={style.typography}>
        <strong>{props.title}</strong>
      </Typography>
      <Typography variant="subtitle2" style={style.typography}>
        Salary: {props.salaryMin + "-" + props.salaryMax + " PLN"}
      </Typography>
      <Typography variant="subtitle2" style={style.typography}>
        Main technology: {props.technology}
      </Typography>
      <Typography variant="subtitle2" style={style.typography}>
        Category: {props.category}
      </Typography>
      <Divider />
      <Typography variant="subtitle2" style={style.typography}>
        Company name: <strong>{props.company.name}</strong>
      </Typography>
      <Typography variant="subtitle2" style={style.typography}>
        Company size: {props.company.size}
      </Typography>
      <Typography variant="subtitle2" style={style.typography}>
        Established: {props.company.establishment}
      </Typography>
      <Divider />
      <Box paddingTop={3} paddingLeft={2}>
        <Grid container direction="row" spacing={3}>
          <Grid item>
            <FacebookIcon style={{color: "f57c00"}}/>
          </Grid>
          <Grid item>
            <InstagramIcon style={{color: "f57c00"}}/>
          </Grid>
          <Grid item>
            <TwitterIcon style={{color: "f57c00"}}/>
          </Grid>
          <Grid item>
            <LinkedInIcon style={{color: "f57c00"}}/>
          </Grid>
        </Grid>
      </Box>
    </Paper> 
  );
};

export default companyData;
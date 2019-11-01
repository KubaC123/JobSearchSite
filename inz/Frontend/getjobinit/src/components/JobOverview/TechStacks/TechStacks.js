import React from 'react';
import Paper from '@material-ui/core/Paper';
import Grid from '@material-ui/core/Grid';
import Box from '@material-ui/core/Box';
import Divider from '@material-ui/core/Divider';
import Typography from '@material-ui/core/Typography';
import TechStack from './TechStack/TechStack';

const techStacks = (props) => {

  let style = {
    paper: {
      width: "500px",
      marginLeft: "auto", 
      marginRight: "auto"
    },
    typography: {
      paddingLeft: 10,
      paddingTop: 5,
      paddingBottom: 5
    }
  }

  let techStacks = props.techStacks.map(techStack => {
    return (
      <Grid item>
        <TechStack techStack={techStack} />
      </Grid>
    )
  })

  return (
    <Paper style={style.paper}>
      <Typography variant="h6" style={style.typography}>
        Tech stack
      </Typography>
      <Divider />
      <Box pt={2}>
        <Grid container direction="row" spacing={5}>
          {techStacks}
        </Grid>
      </Box>
    </Paper>
  );
}

export default techStacks;
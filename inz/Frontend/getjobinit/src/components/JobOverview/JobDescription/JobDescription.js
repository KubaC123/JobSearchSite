import React from 'react'
import Paper from '@material-ui/core/Paper';
import Box from '@material-ui/core/Box';
import Divider from '@material-ui/core/Divider';
import Typography from '@material-ui/core/Typography';

const jobDescription = (props) => {

  let style = {
    paper: {
      width: "500px",
      marginLeft: "auto", 
      marginRight: "auto"
    },
    typography: {
      paddingLeft: 10,
      paddingRight: 10,
      paddingTop: 5,
      paddingBottom: 5
    }
  }

  return (
    <Paper style={style.paper}>
      <Typography variant="h6" style={style.typography}>
        Job description
      </Typography>
      <Divider />
      <Box pt={2}>
        <Typography variant="subtitle2" style={style.typography}>
          {props.description}
        </Typography>
      </Box>
    </Paper>
  )
}

export default jobDescription;
import React from 'react';
import Grid from '@material-ui/core/Grid';
import Typography from '@material-ui/core/Typography';
import LensIcon from '@material-ui/icons/Lens';
import Box from '@material-ui/core/Box';

const techStack = (props) => {

  let style = {
    typography: {
      paddingLeft: 10
    }
  }

  let techStack = null;
  let ts = props.techStack;
  let exp = [];
  for(var i=0; i<ts.experienceLevel; i++) {
    exp.push(
      <Grid item>
        <LensIcon style={{color: "f57c00", width: "10px", height: "10px"}}/>
      </Grid>
    )
    

    techStack = (
      <div>
        <Box paddingLeft={3}>
          <Grid container direction="row" spacing={2}>
            {exp}
          </Grid>
        </Box>
        <Typography variant="subtitle2" style={style.typography}>
          {ts.techStack.name}
        </Typography>
      </div>
    );
  }

  return (
    <div>
      {techStack}
    </div>
  )
}

export default techStack;
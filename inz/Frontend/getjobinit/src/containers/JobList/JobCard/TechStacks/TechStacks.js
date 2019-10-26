import React from 'react'
import Chip from '@material-ui/core/Chip';
import Grid from '@material-ui/core/Grid';
import Box from '@material-ui/core/Box';

const techStacks = (props) => {

  let techStackChips = null;

  if(props.techStacks) {
    var chunkedTechStacks = [];   
    for(var i = 0; i < 6; i += 3) {
      chunkedTechStacks.push(props.techStacks.slice(i, i + 3));
    }
    techStackChips = chunkedTechStacks.map(techStacks => {
      return (
        <Box pt={2}>
          <Grid container direction="row" spacing={2}>
            {techStacks.map(techStack => (
              <Grid item>
                <Chip label={techStack.name}/>
              </Grid>
            ))}
          </Grid>
        </Box>
      );
    })
  }

  return (
    <Grid item>
      <Box m="auto">
      <Grid container direction="column" spacing={1}>
        {techStackChips}
      </Grid>
      </Box>
    </Grid>
  );
}

export default techStacks;
import React from 'react';
import Box from '@material-ui/core/Box';
import Paper from '@material-ui/core/Paper';
import Toolbar from '../Navigation/Toolbar/Toolbar';

const layout = (props) => {
  return (
    <Box>
      <Toolbar />
      <Paper>
        {props.children}
      </Paper>
    </Box>
  );
}

export default layout;
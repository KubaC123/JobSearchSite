import React from 'react';
import Box from '@material-ui/core/Box';
import Toolbar from '../Navigation/Toolbar/Toolbar';

const layout = (props) => {
  return (
    <Box>
      <Toolbar />
      <Box>
        {props.children}
      </Box>
    </Box>
  );
}

export default layout;
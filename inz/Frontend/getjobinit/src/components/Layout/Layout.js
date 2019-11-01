import React from 'react';
import Box from '@material-ui/core/Box';
import Paper from '@material-ui/core/Paper';
import Toolbar from '../Navigation/Toolbar/Toolbar';

const layout = (props) => {
  return (
    <div>
      <Toolbar />
      {props.children}
    </div>
  );
}

export default layout;
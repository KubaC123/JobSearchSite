import React from 'react'
import Box from '@material-ui/core/Box';
import TextField from '@material-ui/core/TextField';
import Tooltip from '@material-ui/core/Tooltip';

const fullTextSearchField = (props) => {

  return (
    <Box display="flex" justifyContent="center">
      <Box pl={3} pr={3}>
        <Tooltip title="Use our full text search, ex: Python Warszawa Junior">
          <TextField style={{width: '500px'}}
            label="Keywords"
            type="search"
            margin="normal"
            variant="outlined"
          />
        </Tooltip>
      </Box>
    </Box>
  );
}

export default fullTextSearchField;
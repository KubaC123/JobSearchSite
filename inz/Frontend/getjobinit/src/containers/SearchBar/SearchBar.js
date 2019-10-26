import React, { Component } from 'react';
import Box from '@material-ui/core/Box';
import TextField from '@material-ui/core/TextField';
import Tooltip from '@material-ui/core/Tooltip';

class SearchBar extends Component {

  state = {
    searchText: null,
    city: null,
    technology: null,
    expLevel: null
  }

  // todo handle state change

  render() {
    return(
      <Box>
        <Box display="flex" justifyContent="center">
          <Box pl={3} pr={3}>
            <Tooltip title="Type in what's on your mind">
              <TextField style={{width: '400px'}}
                id="outlined-search"
                label="Search text"
                type="search"
                margin="normal"
                variant="outlined"
              />
            </Tooltip>
          </Box>
        </Box>

        <Box display="flex" justifyContent="center">
          <Box pl={3} pr={3}>
            <TextField
              id="outlined-search"
              label="City"
              type="search"
              margin="normal"
              variant="outlined"
            />
          </Box>
          <Box pl={3} pr={3}>
            <TextField
              id="outlined-search"
              label="Technology"
              type="search"
              margin="normal"
              variant="outlined"
            />
          </Box>
          <Box pl={3} pr={3}>
            <TextField
              id="outlined-search"
              label="Exp level"
              type="search"
              margin="normal"
              variant="outlined"
            />
          </Box>
        </Box>
      </Box>
    );
  }
}

export default SearchBar;
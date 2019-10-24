import React, { Component } from 'react';
import SearchIcon from '@material-ui/icons/Search';
import Box from '@material-ui/core/Box';
import TextField from '@material-ui/core/TextField';

class SearchBar extends Component {

  render() {
    return(
      <Box>
        <Box display="flex" justifyContent="center">
          <Box pl={3} pr={3}>
            <TextField style={{width: '400px'}}
              id="outlined-search"
              label="Search text"
              type="search"
              margin="normal"
              variant="outlined"
            />
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
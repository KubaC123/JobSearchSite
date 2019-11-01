import React, { Component } from 'react';
import Box from '@material-ui/core/Box';
import InputLabel from '@material-ui/core/InputLabel';
import Input from '@material-ui/core/Input';
import Select from '@material-ui/core/Select';
import MenuItem from '@material-ui/core/MenuItem';
import FormControl from '@material-ui/core/FormControl';

const searchOptions = (props) => {

  const inputStyle = {
    paddingRight: 2,
    paddingLeft: 2
  }

  let citiesItems = null;
  if(props.cities) {
    citiesItems = props.cities.map(city => {
      return (
        <MenuItem value={city.locationId}>{city.name}</MenuItem>
      )
    })
  }

  let categoriesItems = null;
  if(props.categories) {
    categoriesItems = props.categories.map(category => {
      return (
        <MenuItem value={category.id}>{category.name}</MenuItem>
      )
    })
  }

  let technologiesItems = null;
  if(props.technologies) {
    categoriesItems = props.technologies.map(technology => {
      return (
        <MenuItem value={technology.id}>{technology.name}</MenuItem>
      )
    })
  }

  return (
    <Box maxWidth="50%" mx="auto" paddingTop={6}>
      <Box display="flex" justifyContent="center">
        <Box style={inputStyle}>
          <FormControl>
            <InputLabel>City</InputLabel>
            <Input />
          </FormControl>
        </Box>
        <Box style={inputStyle}>
          <FormControl>
            <InputLabel>Category</InputLabel>
            <Input/>
          </FormControl>
        </Box>
        <Box style={inputStyle}>
          <FormControl>
            <InputLabel>Technology</InputLabel>
            <Input/>
          </FormControl>
        </Box>
        <Box style={inputStyle}>
          <FormControl>
            <InputLabel>Exp level</InputLabel>
            <Select style={{minWidth: 100}}>
              <MenuItem>Junior</MenuItem>
              <MenuItem>Mid</MenuItem>
              <MenuItem>Senior</MenuItem>
            </Select>
            </FormControl>
        </Box>
      </Box>
    </Box>
  )
}

export default searchOptions;
import React, { Component } from 'react';
import Box from '@material-ui/core/Box';
import Button from '@material-ui/core/Button';
import FullTextSearchField from './FullTextSearchField/FullTextSearchField';
import getJobInItClient from '../../../GetJobInItClient';

class SearchBar extends Component {

  state = {
    searchInput: {
      searchText: null,
      city: null,
      category: null,
      technology: null,
      expLevel: null
    },
    options: {
      cities: [],
      categories: [],
      technologies: []
    }
  }

  getCities() {
    getJobInItClient.get("/job-service/api/location/all")
    .then(response => {
      const cities = response.data.map(location => {
        return {
          locationId: location.id,
          name: location.city
        }
      })
      console.log(cities);
      this.setState({options: {cities: cities}});
    })
  }

  getCategories() {
    getJobInItClient.get("/job-service/api/category/all")
    .then(response => {
      const categories = response.data;
      this.setState({options: {categories: categories}});
    })
  }

  getTechnologies() {
    getJobInItClient.get("/job-service/api/technology/all")
    .then(response => {
      const technologies = response.data;
      this.setState({options: {technologies: technologies}});
    })
  }

  handleSearchTextChange = (event) => {
    let searchText = event.target.value;
    this.setState({searchInput: { searchText: searchText}});
  }

  handleSearchClicked = (event) => {
    if(this.state.searchInput.searchText) {
      this.props.handleSearch(this.state.searchInput.searchText);
    }
  }

  componentDidMount() {
    // this.getCities();
    // this.getCategories();
    // this.getTechnologies();
  }

  render() {
    return(
      <Box>
        <FullTextSearchField
          handleSearchTextChange={this.handleSearchTextChange}
        />
        <Box display="flex" justifyContent="center" paddingTop={1}>
          <Button 
            style={{minWidth: 400}} 
            variant="contained"
            onClick={this.handleSearchClicked}>
            Search
          </Button>
        </Box>
      </Box>
    );
  }
}

export default SearchBar;
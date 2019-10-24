import React from 'react';
import Layout from './components/Layout/Layout';
import SearchBar from './containers/SearchBar/SearchBar';
import JobList from './containers/JobList/JobList';
import { MuiThemeProvider, createMuiTheme } from '@material-ui/core/styles';
import blue from '@material-ui/core/colors/blue';
import orange from '@material-ui/core/colors/orange';
import lime from '@material-ui/core/colors/lime';
import CssBaseline from '@material-ui/core/CssBaseline';

const theme = createMuiTheme({
  spacing: 4,
  palette: {
    type: 'dark',
    background: {
      default: 'black'
    },
    primary: blue,
    secondary: orange
  },
});

function App() {
  return (
    <MuiThemeProvider theme={theme}>
      <CssBaseline />
      <Layout>
        <SearchBar />
        <JobList />
      </Layout>
    </MuiThemeProvider>
  );
}

export default App;

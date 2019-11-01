import React from 'react';
import Layout from './components/Layout/Layout';
import MainScreen from './containers/MainScreen/MainScreen';
import { MuiThemeProvider, createMuiTheme } from '@material-ui/core/styles';
import blue from '@material-ui/core/colors/blue';
import orange from '@material-ui/core/colors/orange';
import indigo from '@material-ui/core/colors/indigo';
import CssBaseline from '@material-ui/core/CssBaseline';
import { BrowserRouter } from 'react-router-dom';

const theme = createMuiTheme({
  spacing: 4,
  typography: {
    fontFamily: 'sans-serif'
  },
  palette: {
    type: 'light',
    // background: {
    //   default: 'black'
    // },
    primary: {
      main: orange[700] , //90caf9
      box: blue[50], //e3f2fd
      paper: indigo[100] //c5cae9
    },
    secondary: {
      main: orange[700] //f57c00
    }
  },
});

function App() {
  return (
    <MuiThemeProvider theme={theme}>
      <CssBaseline />
      <Layout>
        <BrowserRouter>
          <MainScreen />
        </BrowserRouter>
      </Layout>
    </MuiThemeProvider>
  );
}

export default App;

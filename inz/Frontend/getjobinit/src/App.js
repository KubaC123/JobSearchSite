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
      main: blue[700] , //90caf9
    },
    secondary: {
      main: blue[700] //f57c00
    }
  },
});

function App() {
  return (
    <MuiThemeProvider theme={theme}>
      <CssBaseline />
        <BrowserRouter>
          <Layout>
            <MainScreen />
          </Layout>
      </BrowserRouter>
    </MuiThemeProvider>
  );
}

export default App;

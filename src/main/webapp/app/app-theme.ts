import { createMuiTheme } from '@material-ui/core';

export default createMuiTheme({
  typography: {
    fontFamily: ['"Bungee Inline"', '"Work Sans"'].join(','),
    body2: {
      fontFamily: 'Work Sans'
    },
  },
  palette: {
    background: {
      'default': '#e6e5e5'
    },
    primary: {
      light: '#333333',
      main: '#1a1a1a',
      dark: '#0a0a0a',
      contrastText: '#449DD1',
    },
    secondary: {
      light: '#7cbbdf',
      main: '#449DD1',
      dark: '#2c82b5',
      contrastText: '#fff',
    },
    error: {
      light: '#d71d2d',
      main: '#a31621',
      dark: '#6c0f16'
    },
    success: {
      light: '#00f5af',
      main: '#00A878',
      dark: '#007A58'
    },
    warning: {
      light: '#CF9577',
      main: '#BE6E46',
      dark: '#975635'
    },
    info: {
      light: '#ddddf8',
      main: '#9F9FED',
      dark: '#7676e5'
    }
  },
});

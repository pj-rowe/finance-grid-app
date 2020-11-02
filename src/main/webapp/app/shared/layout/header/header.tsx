import './header.scss';

import React, { useState } from 'react';
import { Translate, Storage } from 'react-jhipster';
import { Navbar, Nav, NavbarToggler, NavbarBrand, Collapse } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import {
  FcOrgUnit as BrandIcon,
  AiOutlineAppstore as HomeIcon,
  FaBalanceScaleLeft as BudgetIcon,
  AiOutlineCalendar as BillsIcon,
  AiOutlineFundView as LoansIcon,
  AiOutlineExperiment as DinneryIcon,
  AiOutlineVideoCamera as MovieWatchlistIcon,
  GoSettings as SettingsIcon,
  GiExitDoor as LogoutIcon
} from "react-icons/all";

import { NavLink as Link, NavLink } from 'react-router-dom';
import LoadingBar from 'react-redux-loading-bar';

import { Home, Brand } from './header-components';
import {AdminMenu, EntitiesMenu, AccountMenu, LocaleMenu, NewLocaleMenu } from '../menus';
import { makeStyles, Theme, createStyles, AppBar, Toolbar, IconButton, Typography, List, Divider, Button } from '@material-ui/core';
import MenuIcon from '@material-ui/icons/Menu';

export interface IHeaderProps {
  isAuthenticated: boolean;
  isAdmin: boolean;
  ribbonEnv: string;
  isInProduction: boolean;
  isSwaggerEnabled: boolean;
  currentLocale: string;
  onLocaleChange: Function;
}

const useStyles = makeStyles((theme: Theme) =>
  createStyles({
    grow: {
      flexGrow: 1
    },
    menuButton: {
      marginRight: theme.spacing(2)
    },
    navSection: {
      display: 'flex',
      marginLeft: theme.spacing(1),
      marginRight: theme.spacing(1)
    },
    divider: {
      width: '4px',
      marginLeft: theme.spacing(2),
      marginRight: theme.spacing(2),
      backgroundColor: theme.palette.background.default
    },
    navLink: {
      marginLeft: theme.spacing(1),
      marginRight: theme.spacing(1),
      color: theme.palette.background.default,
      textDecoration: 'none',
      '&.active': {
        color: theme.palette.primary.contrastText,
        textDecoration: 'none',
      },
      '&:hover': {
        color: theme.palette.background.default,
        textDecoration: 'underline dotted',
      }
    },
    settingsButton: {
      color: theme.palette.warning.main,
      '&:hover': {
        backgroundColor: theme.palette.warning.main,
        color: theme.palette.primary.main,
      }
    },
    logoutButton: {
      marginLeft: '5px',
      color: theme.palette.secondary.main,
      '&:hover': {
        backgroundColor: theme.palette.secondary.main,
        color: theme.palette.primary.main,
      },
    }
  })
)

const Header = (props: IHeaderProps) => {
  const [menuOpen, setMenuOpen] = useState(false);

  const handleLocaleChange = event => {
    const langKey = event.target.value;
    Storage.session.set('locale', langKey);
    props.onLocaleChange(langKey);
  };

  const renderDevRibbon = () =>
    props.isInProduction === false ? (
      <div className="ribbon dev">
        <a href="">
          <Translate contentKey={`global.ribbon.${props.ribbonEnv}`} />
        </a>
      </div>
    ) : null;

  const toggleMenu = () => setMenuOpen(!menuOpen);

  /* jhipster-needle-add-element-to-menu - JHipster will add new menu items here */
  const classes = useStyles();

  return (
    <>
      <div className={classes.grow}>
        <AppBar position="static">
          <Toolbar>
            <IconButton
              edge="start"
              className={classes.menuButton}
              color="inherit"
              aria-label="open drawer"
            >
              <MenuIcon />
            </IconButton>
            <Typography variant="h4" noWrap>Finance Grid</Typography>
            <div className={classes.grow} />
            <div className={classes.navSection}>
              <NavLink className={ classes.navLink } to="/budget" activeClassName="active">
                <Typography variant="h6" noWrap>Budget Boss</Typography>
              </NavLink>
              <NavLink className={ classes.navLink }  to="/loan" activeClassName="active">
                <Typography variant="h6" noWrap>Loan Sentinel</Typography>
              </NavLink>
              <NavLink className={ classes.navLink }  to="/bills" activeClassName="active">
                <Typography variant="h6" noWrap>Bill Tracker</Typography>
              </NavLink>
            </div>
            <Divider orientation="vertical" className={ classes.divider } flexItem />
            <div className={classes.navSection}>
              <NewLocaleMenu currentLocale={ props.currentLocale } onClick={handleLocaleChange} />
              <Button
                className={ classes.settingsButton }
                startIcon={ <SettingsIcon size={24}/> }
              >
                Admin
              </Button>
              <Button
                className={ classes.logoutButton }
                startIcon={ <LogoutIcon size={24}/> }
                // onClick={ async () => { await Auth.signOut(); window.location.reload(); } }
              >
                Sign Out
              </Button>
            </div>
          </Toolbar>
        </AppBar>
      </div>
      <div id="app-header">
        {renderDevRibbon()}
        <LoadingBar className="loading-bar" />
        <Navbar dark expand="sm" fixed="top" className="jh-navbar">
          <NavbarToggler aria-label="Menu" onClick={toggleMenu} />
          <Brand />
          <Collapse isOpen={menuOpen} navbar>
            <Nav id="header-tabs" className="ml-auto" navbar>
              <Home />
              {props.isAuthenticated && <EntitiesMenu />}
              {props.isAuthenticated && props.isAdmin && <AdminMenu showSwagger={props.isSwaggerEnabled} />}
              <LocaleMenu currentLocale={props.currentLocale} onClick={handleLocaleChange} />
              <AccountMenu isAuthenticated={props.isAuthenticated} />
            </Nav>
          </Collapse>
        </Navbar>
      </div>
    </>
  );
};

export default Header;

import React from 'react';
import MenuItem from 'app/shared/layout/menus/menu-item';
import { DropdownItem } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { NavLink as Link } from 'react-router-dom';
import { Translate, translate } from 'react-jhipster';
import { NavDropdown } from './menu-components';
import {
  makeStyles,
  Theme,
  createStyles,
  Button,
  Popper,
  Grow,
  Paper,
  ClickAwayListener,
  MenuList,
  MenuItem as MaterialUIMenuItem
} from '@material-ui/core';
import {
  FaUserAstronaut as AccountIcon,
  BiLogIn as SignInIcon,
  BiWrench as AccountSettingsIcon,
  BiLockAlt as PasswordIcon,
  BiLogOut as SignOutIcon
} from "react-icons/all";

const accountMenuItemsAuthenticated = (
  <>
    <MenuItem icon="wrench" to="/account/settings">
      <Translate contentKey="global.menu.account.settings">Settings</Translate>
    </MenuItem>
    <MenuItem icon="lock" to="/account/password">
      <Translate contentKey="global.menu.account.password">Password</Translate>
    </MenuItem>
    <MenuItem icon="sign-out-alt" to="/logout">
      <Translate contentKey="global.menu.account.logout">Sign out</Translate>
    </MenuItem>
  </>
);

const accountMenuItems = (
  <>
    <MenuItem id="login-item" icon="sign-in-alt" to="/login">
      <Translate contentKey="global.menu.account.login">Sign in</Translate>
    </MenuItem>
    <MenuItem icon="sign-in-alt" to="/account/register">
      <Translate contentKey="global.menu.account.register">Register</Translate>
    </MenuItem>
  </>
);

const accountMenuLinks = [
  {
    key: 'global.menu.account.login',
    icon: <SignInIcon size={15} style={{ marginRight: '5px' }} />,
    title: 'Sign in',
    path: '/login',
    requiresAuth: false
  },
  {
    key: 'global.menu.account.settings',
    icon: <AccountSettingsIcon size={15} style={{ marginRight: '5px' }} />,
    title: 'Settings',
    path: '/account/settings',
    requiresAuth: true
  },
  {
    key: 'global.menu.account.password',
    icon: <PasswordIcon size={15} style={{ marginRight: '5px' }} />,
    title: 'Password',
    path: '/account/password',
    requiresAuth: true
  },
  {
    key: 'global.menu.account.logout',
    icon: <SignOutIcon size={15} style={{ marginRight: '5px' }} />,
    title: 'Sign Out',
    path: '/logout',
    requiresAuth: true
  }
];

export const NewAccountMenu = ({ isAuthenticated = false }) => {
  const classes = makeStyles((theme: Theme) =>
    createStyles({
      accountButton: {
        marginleft: theme.spacing(1),
        marginRight: theme.spacing(1),
        color: theme.palette.secondary.main,
        borderColor: theme.palette.secondary.main,
        '&:hover': {
          backgroundColor: theme.palette.secondary.main,
          color: theme.palette.primary.main,
        },
        '&:focus': {
          outline: 0
        }
      },
      accountMenu: {
        zIndex: 1,
        marginTop: '15px'
      },
      accountMenuPaper: {
        backgroundColor: theme.palette.primary.main,
        color: theme.palette.secondary.main
      },
      accountMenuItem: {
        display: 'flex',
        alignItems: 'center',
        fontSize: '0.875rem',
        fontWeight: 'normal',
        color: theme.palette.secondary.main,
        '&:hover': {
          color: theme.palette.secondary.main,
          textDecoration: 'underline dotted'
        }
      }
    })
  )();

  const [open, setOpen] = React.useState(false);
  const anchorRef = React.useRef<HTMLButtonElement>(null);

  const handleToggle = () => {
    setOpen((prevOpen) => !prevOpen);
  };

  const handleClose = (event: React.MouseEvent<EventTarget>) => {
    if (anchorRef.current && anchorRef.current.contains(event.target as HTMLElement)) {
      return;
    }

    setOpen(false);
  };

  function handleListKeyDown(event: React.KeyboardEvent) {
    if (event.key === 'Tab') {
      event.preventDefault();
      setOpen(false);
    }
  }

  // return focus to the button when we transitioned from !open -> open
  const prevOpen = React.useRef(open);
  React.useEffect(() => {
    if (prevOpen.current === true && open === false) {
      anchorRef.current!.focus();
    }

    prevOpen.current = open;
  }, [open]);

  return (
    <>
      <Button
        variant="outlined"
        className={ classes.accountButton }
        startIcon={<AccountIcon size={24}/>}
        ref={anchorRef}
        onClick={ handleToggle }
        aria-controls={ open ? 'menu-list-grow' : undefined }
        aria-haspopup="true"
      >
        { translate('global.menu.account.main') }
      </Button>
      <Popper
        className={ classes.accountMenu }
        open={open}
        anchorEl={anchorRef.current}
        placement="bottom-end"
        role={undefined}
        transition disablePortal
      >
        {({ TransitionProps }) => (
          <Grow {...TransitionProps}>
            <Paper className={ classes.accountMenuPaper }>
              <ClickAwayListener onClickAway={handleClose}>
                <MenuList autoFocusItem={open} id="menu-list-grow" onKeyDown={handleListKeyDown}>
                  {
                    accountMenuLinks.filter(l => l.requiresAuth === isAuthenticated)
                      .map(l => (
                        <MaterialUIMenuItem key={l.key} onClick={handleClose}>
                          <Link className={ classes.accountMenuItem } to={l.path}>
                            { l.icon }
                            <Translate contentKey={l.key}>{ l.title }</Translate>
                          </Link>
                        </MaterialUIMenuItem>
                      ))
                  }
                </MenuList>
              </ClickAwayListener>
            </Paper>
          </Grow>
        )}
      </Popper>
    </>
  )
};

export const AccountMenu = ({ isAuthenticated = false }) => (
  <NavDropdown icon="user" name={translate('global.menu.account.main')} id="account-menu">
    {isAuthenticated ? accountMenuItemsAuthenticated : accountMenuItems}
  </NavDropdown>
);

export default AccountMenu;

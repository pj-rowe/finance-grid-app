import React from 'react';
import MenuItem from 'app/shared/layout/menus/menu-item';
import { DropdownItem } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { Link } from 'react-router-dom';
import { NavDropdown } from './menu-components';
import { Translate, translate } from 'react-jhipster';
import {
  makeStyles,
  Theme,
  createStyles,
  Grow,
  Paper,
  ClickAwayListener,
  MenuList,
  Popper,
  MenuItem as MaterialUIMenuItem,
  IconButton
} from '@material-ui/core';
import {
  GoSettings as AdminIcon,
  HiUserGroup as UserManagementIcon,
  HiChartBar as MetricsIcon,
  HiHeart as HealthIcon,
  HiCog as ConfigurationIcon,
  HiViewList as AuditsIcon,
  HiCollection as LogsIcon,
  HiBookOpen as DocsIcon
} from "react-icons/all";

const adminMenuItems = (
  <>
    <MenuItem icon="user" to="/admin/user-management">
      <Translate contentKey="global.menu.admin.userManagement">User management</Translate>
    </MenuItem>
    <MenuItem icon="tachometer-alt" to="/admin/metrics">
      <Translate contentKey="global.menu.admin.metrics">Metrics</Translate>
    </MenuItem>
    <MenuItem icon="heart" to="/admin/health">
      <Translate contentKey="global.menu.admin.health">Health</Translate>
    </MenuItem>
    <MenuItem icon="list" to="/admin/configuration">
      <Translate contentKey="global.menu.admin.configuration">Configuration</Translate>
    </MenuItem>
    <MenuItem icon="bell" to="/admin/audits">
      <Translate contentKey="global.menu.admin.audits">Audits</Translate>
    </MenuItem>
    {/* jhipster-needle-add-element-to-admin-menu - JHipster will add entities to the admin menu here */}
    <MenuItem icon="tasks" to="/admin/logs">
      <Translate contentKey="global.menu.admin.logs">Logs</Translate>
    </MenuItem>
  </>
);

const adminMenuLinks = [
  {
    key: 'global.menu.admin.userManagement',
    icon: <UserManagementIcon size={15} style={{ marginRight: '5px' }} />,
    title: 'User management',
    path: '/admin/user-management'
  },
  {
    key: 'global.menu.admin.metrics',
    icon: <MetricsIcon size={15} style={{ marginRight: '5px' }} />,
    title: 'Metrics',
    path: '/admin/metrics'
  },
  {
    key: 'global.menu.admin.health',
    icon: <HealthIcon size={15} style={{ marginRight: '5px' }} />,
    title: 'Health',
    path: '/admin/health'
  },
  {
    key: 'global.menu.admin.configuration',
    icon: <ConfigurationIcon size={15} style={{ marginRight: '5px' }} />,
    title: 'Configuration',
    path: '/admin/configuration'
  },
  {
    key: 'global.menu.admin.audits',
    icon: <AuditsIcon size={15} style={{ marginRight: '5px' }} />,
    title: 'Audits',
    path: '/admin/audits'
  },
  {
    key: 'global.menu.admin.logs',
    icon: <LogsIcon size={15} style={{ marginRight: '5px' }} />,
    title: 'Logs',
    path: '/admin/logs'
  },
  {
    key: 'global.menu.admin.apidocs',
    icon: <DocsIcon size={15} style={{ marginRight: '5px' }} />,
    title: 'API',
    path: '/admin/docs'
  }
];

const swaggerItem = (
  <MenuItem icon="book" to="/admin/docs">
    <Translate contentKey="global.menu.admin.apidocs">API</Translate>
  </MenuItem>
);

export const NewAdminMenu = ({ showSwagger }) => {
  const classes = makeStyles((theme: Theme) =>
    createStyles({
      adminButton: {
        marginleft: theme.spacing(1),
        marginRight: theme.spacing(1),
        color: theme.palette.warning.main,
        borderColor: theme.palette.warning.main,
        '&:hover': {
          backgroundColor: theme.palette.warning.main,
          color: theme.palette.primary.main,
        },
        '&:focus': {
          outline: 0
        }
      },
      adminMenu: {
        zIndex: 1,
        marginTop: '10px'
      },
      adminMenuPaper: {
        backgroundColor: theme.palette.primary.main,
        color: theme.palette.warning.main
      },
      adminMenuItem: {
        display: 'flex',
        alignItems: 'center',
        fontSize: '0.875rem',
        fontWeight: 'normal',
        color: theme.palette.warning.main,
        '&:hover': {
          color: theme.palette.warning.main,
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
      <IconButton
        className={ classes.adminButton }
        ref={anchorRef}
        onClick={ handleToggle }
        aria-controls={ open ? 'menu-list-grow' : undefined }
        aria-haspopup="true"
      >
        <AdminIcon size={24}/>
      </IconButton>
      <Popper
        className={ classes.adminMenu }
        open={open}
        anchorEl={anchorRef.current}
        placement="bottom-end"
        role={undefined}
        transition disablePortal
      >
        {({ TransitionProps }) => (
          <Grow {...TransitionProps}>
            <Paper className={ classes.adminMenuPaper }>
              <ClickAwayListener onClickAway={handleClose}>
                <MenuList autoFocusItem={open} id="menu-list-grow" onKeyDown={handleListKeyDown}>
                  {
                    adminMenuLinks.filter(l => l.title !== 'API' || showSwagger)
                      .map(l => (
                        <MaterialUIMenuItem key={l.key} onClick={handleClose}>
                          <Link className={ classes.adminMenuItem } to={l.path}>
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
  );
}

export const AdminMenu = ({ showSwagger }) => (
  <NavDropdown icon="user-plus" name={translate('global.menu.admin.main')} style={{ width: '140%' }} id="admin-menu">
    {adminMenuItems}
    {showSwagger && swaggerItem}
  </NavDropdown>
);

export default AdminMenu;

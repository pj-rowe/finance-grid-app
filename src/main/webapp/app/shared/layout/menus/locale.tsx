import React from 'react';
import { DropdownItem } from 'reactstrap';
import { NavDropdown } from './menu-components';
import { locales, languages } from 'app/config/translation';
import { Button, makeStyles, createStyles, Theme, Menu, MenuItem, Popper, Grow, Paper, ClickAwayListener, MenuList } from '@material-ui/core';
import { AiOutlineFlag as  LocaleIcon } from 'react-icons/ai';

export const NewLocaleMenu = ({ currentLocale, onClick }: { currentLocale: string; onClick: Function }) => {

  const classes = makeStyles((theme: Theme) =>
    createStyles({
      localeButton: {
        marginleft: theme.spacing(1),
        marginRight: theme.spacing(1),
        color: theme.palette.success.main,
        borderColor: theme.palette.success.main,
        '&:hover': {
          backgroundColor: theme.palette.success.main,
          color: theme.palette.primary.main,
        },
        '&:focus': {
          outline: 0
        }
      },
      localeMenu: {
        zIndex: 1,
        marginTop: '15px'
      },
      localeMenuPaper: {
        backgroundColor: theme.palette.primary.main,
        color: theme.palette.success.main
      },
      localeMenuItem: {
        fontSize: '0.875rem'
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

  return Object.keys(languages).length > 1 ? (
    <>
      <Button
        variant="outlined"
        className={ classes.localeButton }
        startIcon={<LocaleIcon size={24}/>}
        ref={anchorRef}
        onClick={ handleToggle }
        aria-controls={ open ? 'menu-list-grow' : undefined }
        aria-haspopup="true"
      >
        {currentLocale ? languages[currentLocale].name : undefined}
      </Button>
      <Popper
        className={ classes.localeMenu }
        open={open}
        anchorEl={anchorRef.current}
        placement="bottom-end"
        role={undefined}
        transition disablePortal
      >
        {({ TransitionProps }) => (
          <Grow {...TransitionProps}>
            <Paper className={ classes.localeMenuPaper }>
              <ClickAwayListener onClickAway={handleClose}>
                <MenuList autoFocusItem={open} id="menu-list-grow" onKeyDown={handleListKeyDown}>
                  {
                    locales.map(locale => (
                      <MenuItem
                        key={languages[locale].name}
                        className={ classes.localeMenuItem }
                        value={locale}
                        onClick={ (ev) => { onClick({ target: { value: locale }}); handleClose(ev) } }
                      >
                        { languages[locale].name }
                      </MenuItem>
                    ))
                  }
                </MenuList>
              </ClickAwayListener>
            </Paper>
          </Grow>
        )}
      </Popper>
    </>
  ) : null;
}

export const LocaleMenu = ({ currentLocale, onClick }: { currentLocale: string; onClick: Function }) =>
  Object.keys(languages).length > 1 ? (
    <NavDropdown icon="flag" name={currentLocale ? languages[currentLocale].name : undefined}>
      {locales.map(locale => (
        <DropdownItem key={locale} value={locale} onClick={onClick}>
          {languages[locale].name}
        </DropdownItem>
      ))}
    </NavDropdown>
  ) : null;

import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import LoanPrincipal from './loan-principal';
import LoanPrincipalDetail from './loan-principal-detail';
import LoanPrincipalUpdate from './loan-principal-update';
import LoanPrincipalDeleteDialog from './loan-principal-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={LoanPrincipalUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={LoanPrincipalUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={LoanPrincipalDetail} />
      <ErrorBoundaryRoute path={match.url} component={LoanPrincipal} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={LoanPrincipalDeleteDialog} />
  </>
);

export default Routes;

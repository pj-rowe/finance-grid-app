import React from 'react';
import { Switch } from 'react-router-dom';

// eslint-disable-next-line @typescript-eslint/no-unused-vars
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Budget from './budget';
import Item from './item';
import Loan from './loan';
import LoanPrincipal from './loan-principal';
/* jhipster-needle-add-route-import - JHipster will add routes here */

const Routes = ({ match }) => (
  <div>
    <Switch>
      {/* prettier-ignore */}
      <ErrorBoundaryRoute path={`${match.url}budget`} component={Budget} />
      <ErrorBoundaryRoute path={`${match.url}item`} component={Item} />
      <ErrorBoundaryRoute path={`${match.url}loan`} component={Loan} />
      <ErrorBoundaryRoute path={`${match.url}loan-principal`} component={LoanPrincipal} />
      {/* jhipster-needle-add-route-path - JHipster will add routes here */}
    </Switch>
  </div>
);

export default Routes;

import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './loan-principal.reducer';
import { ILoanPrincipal } from 'app/shared/model/loan-principal.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ILoanPrincipalDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const LoanPrincipalDetail = (props: ILoanPrincipalDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { loanPrincipalEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          <Translate contentKey="financeGridApp.loanPrincipal.detail.title">LoanPrincipal</Translate> [<b>{loanPrincipalEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="date">
              <Translate contentKey="financeGridApp.loanPrincipal.date">Date</Translate>
            </span>
          </dt>
          <dd>
            {loanPrincipalEntity.date ? <TextFormat value={loanPrincipalEntity.date} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="amount">
              <Translate contentKey="financeGridApp.loanPrincipal.amount">Amount</Translate>
            </span>
          </dt>
          <dd>{loanPrincipalEntity.amount}</dd>
          <dt>
            <Translate contentKey="financeGridApp.loanPrincipal.loan">Loan</Translate>
          </dt>
          <dd>{loanPrincipalEntity.loan ? loanPrincipalEntity.loan.name : ''}</dd>
        </dl>
        <Button tag={Link} to="/loan-principal" replace color="info">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/loan-principal/${loanPrincipalEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ loanPrincipal }: IRootState) => ({
  loanPrincipalEntity: loanPrincipal.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(LoanPrincipalDetail);

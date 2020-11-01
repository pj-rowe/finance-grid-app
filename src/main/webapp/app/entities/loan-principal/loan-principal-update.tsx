import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { ILoan } from 'app/shared/model/loan.model';
import { getEntities as getLoans } from 'app/entities/loan/loan.reducer';
import { getEntity, updateEntity, createEntity, reset } from './loan-principal.reducer';
import { ILoanPrincipal } from 'app/shared/model/loan-principal.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface ILoanPrincipalUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const LoanPrincipalUpdate = (props: ILoanPrincipalUpdateProps) => {
  const [loanId, setLoanId] = useState('0');
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { loanPrincipalEntity, loans, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/loan-principal' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getLoans();
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const entity = {
        ...loanPrincipalEntity,
        ...values,
      };

      if (isNew) {
        props.createEntity(entity);
      } else {
        props.updateEntity(entity);
      }
    }
  };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="financeGridApp.loanPrincipal.home.createOrEditLabel">
            <Translate contentKey="financeGridApp.loanPrincipal.home.createOrEditLabel">Create or edit a LoanPrincipal</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : loanPrincipalEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="loan-principal-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="loan-principal-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="dateLabel" for="loan-principal-date">
                  <Translate contentKey="financeGridApp.loanPrincipal.date">Date</Translate>
                </Label>
                <AvField
                  id="loan-principal-date"
                  type="date"
                  className="form-control"
                  name="date"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="amountLabel" for="loan-principal-amount">
                  <Translate contentKey="financeGridApp.loanPrincipal.amount">Amount</Translate>
                </Label>
                <AvField
                  id="loan-principal-amount"
                  type="text"
                  name="amount"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                    number: { value: true, errorMessage: translate('entity.validation.number') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label for="loan-principal-loan">
                  <Translate contentKey="financeGridApp.loanPrincipal.loan">Loan</Translate>
                </Label>
                <AvInput
                  id="loan-principal-loan"
                  type="select"
                  className="form-control"
                  name="loan.id"
                  value={isNew ? loans[0] && loans[0].id : loanPrincipalEntity.loan?.id}
                  required
                >
                  {loans
                    ? loans.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.name}
                        </option>
                      ))
                    : null}
                </AvInput>
                <AvFeedback>
                  <Translate contentKey="entity.validation.required">This field is required.</Translate>
                </AvFeedback>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/loan-principal" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </AvForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

const mapStateToProps = (storeState: IRootState) => ({
  loans: storeState.loan.entities,
  loanPrincipalEntity: storeState.loanPrincipal.entity,
  loading: storeState.loanPrincipal.loading,
  updating: storeState.loanPrincipal.updating,
  updateSuccess: storeState.loanPrincipal.updateSuccess,
});

const mapDispatchToProps = {
  getLoans,
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(LoanPrincipalUpdate);

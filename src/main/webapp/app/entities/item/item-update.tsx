import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IBudget } from 'app/shared/model/budget.model';
import { getEntities as getBudgets } from 'app/entities/budget/budget.reducer';
import { getEntity, updateEntity, createEntity, reset } from './item.reducer';
import { IItem } from 'app/shared/model/item.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IItemUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const ItemUpdate = (props: IItemUpdateProps) => {
  const [budgetId, setBudgetId] = useState('0');
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { itemEntity, budgets, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/item' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getBudgets();
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const entity = {
        ...itemEntity,
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
          <h2 id="financeGridApp.item.home.createOrEditLabel">
            <Translate contentKey="financeGridApp.item.home.createOrEditLabel">Create or edit a Item</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : itemEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="item-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="item-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="nameLabel" for="item-name">
                  <Translate contentKey="financeGridApp.item.name">Name</Translate>
                </Label>
                <AvField
                  id="item-name"
                  type="text"
                  name="name"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                    minLength: { value: 3, errorMessage: translate('entity.validation.minlength', { min: 3 }) },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="itemTypeLabel" for="item-itemType">
                  <Translate contentKey="financeGridApp.item.itemType">Item Type</Translate>
                </Label>
                <AvInput
                  id="item-itemType"
                  type="select"
                  className="form-control"
                  name="itemType"
                  value={(!isNew && itemEntity.itemType) || 'ASSET'}
                >
                  <option value="ASSET">{translate('financeGridApp.ItemType.ASSET')}</option>
                  <option value="EXPENSE">{translate('financeGridApp.ItemType.EXPENSE')}</option>
                  <option value="SAVING">{translate('financeGridApp.ItemType.SAVING')}</option>
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label id="dueDateLabel" for="item-dueDate">
                  <Translate contentKey="financeGridApp.item.dueDate">Due Date</Translate>
                </Label>
                <AvField id="item-dueDate" type="date" className="form-control" name="dueDate" />
              </AvGroup>
              <AvGroup check>
                <Label id="paidLabel">
                  <AvInput id="item-paid" type="checkbox" className="form-check-input" name="paid" />
                  <Translate contentKey="financeGridApp.item.paid">Paid</Translate>
                </Label>
              </AvGroup>
              <AvGroup>
                <Label id="expectedAmountLabel" for="item-expectedAmount">
                  <Translate contentKey="financeGridApp.item.expectedAmount">Expected Amount</Translate>
                </Label>
                <AvField
                  id="item-expectedAmount"
                  type="text"
                  name="expectedAmount"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                    number: { value: true, errorMessage: translate('entity.validation.number') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="actualAmountLabel" for="item-actualAmount">
                  <Translate contentKey="financeGridApp.item.actualAmount">Actual Amount</Translate>
                </Label>
                <AvField
                  id="item-actualAmount"
                  type="text"
                  name="actualAmount"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                    number: { value: true, errorMessage: translate('entity.validation.number') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="categoryLabel" for="item-category">
                  <Translate contentKey="financeGridApp.item.category">Category</Translate>
                </Label>
                <AvInput
                  id="item-category"
                  type="select"
                  className="form-control"
                  name="category"
                  value={(!isNew && itemEntity.category) || 'BILL'}
                >
                  <option value="BILL">{translate('financeGridApp.ItemCategory.BILL')}</option>
                  <option value="FUN">{translate('financeGridApp.ItemCategory.FUN')}</option>
                  <option value="MISC">{translate('financeGridApp.ItemCategory.MISC')}</option>
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label for="item-budget">
                  <Translate contentKey="financeGridApp.item.budget">Budget</Translate>
                </Label>
                <AvInput id="item-budget" type="select" className="form-control" name="budget.id">
                  <option value="" key="0" />
                  {budgets
                    ? budgets.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.name}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/item" replace color="info">
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
  budgets: storeState.budget.entities,
  itemEntity: storeState.item.entity,
  loading: storeState.item.loading,
  updating: storeState.item.updating,
  updateSuccess: storeState.item.updateSuccess,
});

const mapDispatchToProps = {
  getBudgets,
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ItemUpdate);

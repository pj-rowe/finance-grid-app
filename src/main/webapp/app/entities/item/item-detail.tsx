import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './item.reducer';
import { IItem } from 'app/shared/model/item.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IItemDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const ItemDetail = (props: IItemDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { itemEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          <Translate contentKey="financeGridApp.item.detail.title">Item</Translate> [<b>{itemEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="name">
              <Translate contentKey="financeGridApp.item.name">Name</Translate>
            </span>
          </dt>
          <dd>{itemEntity.name}</dd>
          <dt>
            <span id="itemType">
              <Translate contentKey="financeGridApp.item.itemType">Item Type</Translate>
            </span>
          </dt>
          <dd>{itemEntity.itemType}</dd>
          <dt>
            <span id="dueDate">
              <Translate contentKey="financeGridApp.item.dueDate">Due Date</Translate>
            </span>
          </dt>
          <dd>{itemEntity.dueDate ? <TextFormat value={itemEntity.dueDate} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="paid">
              <Translate contentKey="financeGridApp.item.paid">Paid</Translate>
            </span>
          </dt>
          <dd>{itemEntity.paid ? 'true' : 'false'}</dd>
          <dt>
            <span id="expectedAmount">
              <Translate contentKey="financeGridApp.item.expectedAmount">Expected Amount</Translate>
            </span>
          </dt>
          <dd>{itemEntity.expectedAmount}</dd>
          <dt>
            <span id="actualAmount">
              <Translate contentKey="financeGridApp.item.actualAmount">Actual Amount</Translate>
            </span>
          </dt>
          <dd>{itemEntity.actualAmount}</dd>
          <dt>
            <span id="category">
              <Translate contentKey="financeGridApp.item.category">Category</Translate>
            </span>
          </dt>
          <dd>{itemEntity.category}</dd>
          <dt>
            <Translate contentKey="financeGridApp.item.budget">Budget</Translate>
          </dt>
          <dd>{itemEntity.budget ? itemEntity.budget.name : ''}</dd>
        </dl>
        <Button tag={Link} to="/item" replace color="info">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/item/${itemEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ item }: IRootState) => ({
  itemEntity: item.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ItemDetail);

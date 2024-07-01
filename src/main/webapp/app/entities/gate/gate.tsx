import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, getSortState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { ASC, DESC, SORT } from 'app/shared/util/pagination.constants';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './gate.reducer';

export const Gate = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const gateList = useAppSelector(state => state.gate.entities);
  const loading = useAppSelector(state => state.gate.loading);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        sort: `${sortState.sort},${sortState.order}`,
      }),
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?sort=${sortState.sort},${sortState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [sortState.order, sortState.sort]);

  const sort = p => () => {
    setSortState({
      ...sortState,
      order: sortState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = sortState.sort;
    const order = sortState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    } else {
      return order === ASC ? faSortUp : faSortDown;
    }
  };

  return (
    <div>
      <h2 id="gate-heading" data-cy="GateHeading">
        <Translate contentKey="cpamainApp.gate.home.title">Gates</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="cpamainApp.gate.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/gate/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="cpamainApp.gate.home.createLabel">Create new Gate</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {gateList && gateList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="cpamainApp.gate.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('name')}>
                  <Translate contentKey="cpamainApp.gate.name">Name</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('name')} />
                </th>
                <th className="hand" onClick={sort('shortName')}>
                  <Translate contentKey="cpamainApp.gate.shortName">Short Name</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('shortName')} />
                </th>
                <th className="hand" onClick={sort('lat')}>
                  <Translate contentKey="cpamainApp.gate.lat">Lat</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('lat')} />
                </th>
                <th className="hand" onClick={sort('lon')}>
                  <Translate contentKey="cpamainApp.gate.lon">Lon</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('lon')} />
                </th>
                <th className="hand" onClick={sort('gateType')}>
                  <Translate contentKey="cpamainApp.gate.gateType">Gate Type</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('gateType')} />
                </th>
                <th className="hand" onClick={sort('isActive')}>
                  <Translate contentKey="cpamainApp.gate.isActive">Is Active</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('isActive')} />
                </th>
                <th>
                  <Translate contentKey="cpamainApp.gate.zone">Zone</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {gateList.map((gate, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/gate/${gate.id}`} color="link" size="sm">
                      {gate.id}
                    </Button>
                  </td>
                  <td>{gate.name}</td>
                  <td>{gate.shortName}</td>
                  <td>{gate.lat}</td>
                  <td>{gate.lon}</td>
                  <td>
                    <Translate contentKey={`cpamainApp.GateType.${gate.gateType}`} />
                  </td>
                  <td>{gate.isActive ? 'true' : 'false'}</td>
                  <td>{gate.zone ? <Link to={`/zone/${gate.zone.id}`}>{gate.zone.name}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/gate/${gate.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/gate/${gate.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() => (window.location.href = `/gate/${gate.id}/delete`)}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="cpamainApp.gate.home.notFound">No Gates found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Gate;

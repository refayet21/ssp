import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, getSortState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { ASC, DESC, SORT } from 'app/shared/util/pagination.constants';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './lane.reducer';

export const Lane = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const laneList = useAppSelector(state => state.lane.entities);
  const loading = useAppSelector(state => state.lane.loading);

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
      <h2 id="lane-heading" data-cy="LaneHeading">
        <Translate contentKey="cpamainApp.lane.home.title">Lanes</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="cpamainApp.lane.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/lane/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="cpamainApp.lane.home.createLabel">Create new Lane</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {laneList && laneList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="cpamainApp.lane.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('name')}>
                  <Translate contentKey="cpamainApp.lane.name">Name</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('name')} />
                </th>
                <th className="hand" onClick={sort('shortName')}>
                  <Translate contentKey="cpamainApp.lane.shortName">Short Name</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('shortName')} />
                </th>
                <th className="hand" onClick={sort('direction')}>
                  <Translate contentKey="cpamainApp.lane.direction">Direction</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('direction')} />
                </th>
                <th className="hand" onClick={sort('isActive')}>
                  <Translate contentKey="cpamainApp.lane.isActive">Is Active</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('isActive')} />
                </th>
                <th>
                  <Translate contentKey="cpamainApp.lane.gate">Gate</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="cpamainApp.lane.accessProfile">Access Profile</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {laneList.map((lane, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/lane/${lane.id}`} color="link" size="sm">
                      {lane.id}
                    </Button>
                  </td>
                  <td>{lane.name}</td>
                  <td>{lane.shortName}</td>
                  <td>
                    <Translate contentKey={`cpamainApp.DirectionType.${lane.direction}`} />
                  </td>
                  <td>{lane.isActive ? 'true' : 'false'}</td>
                  <td>{lane.gate ? <Link to={`/gate/${lane.gate.id}`}>{lane.gate.name}</Link> : ''}</td>
                  <td>
                    {lane.accessProfiles
                      ? lane.accessProfiles.map((val, j) => (
                          <span key={j}>
                            <Link to={`/access-profile/${val.id}`}>{val.id}</Link>
                            {j === lane.accessProfiles.length - 1 ? '' : ', '}
                          </span>
                        ))
                      : null}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/lane/${lane.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/lane/${lane.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() => (window.location.href = `/lane/${lane.id}/delete`)}
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
              <Translate contentKey="cpamainApp.lane.home.notFound">No Lanes found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Lane;

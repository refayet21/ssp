import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, getSortState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { ASC, DESC, SORT } from 'app/shared/util/pagination.constants';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './access-profile.reducer';

export const AccessProfile = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const accessProfileList = useAppSelector(state => state.accessProfile.entities);
  const loading = useAppSelector(state => state.accessProfile.loading);

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
      <h2 id="access-profile-heading" data-cy="AccessProfileHeading">
        <Translate contentKey="cpamainApp.accessProfile.home.title">Access Profiles</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="cpamainApp.accessProfile.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/access-profile/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="cpamainApp.accessProfile.home.createLabel">Create new Access Profile</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {accessProfileList && accessProfileList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="cpamainApp.accessProfile.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('name')}>
                  <Translate contentKey="cpamainApp.accessProfile.name">Name</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('name')} />
                </th>
                <th className="hand" onClick={sort('description')}>
                  <Translate contentKey="cpamainApp.accessProfile.description">Description</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('description')} />
                </th>
                <th className="hand" onClick={sort('startTimeOfDay')}>
                  <Translate contentKey="cpamainApp.accessProfile.startTimeOfDay">Start Time Of Day</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('startTimeOfDay')} />
                </th>
                <th className="hand" onClick={sort('endTimeOfDay')}>
                  <Translate contentKey="cpamainApp.accessProfile.endTimeOfDay">End Time Of Day</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('endTimeOfDay')} />
                </th>
                <th className="hand" onClick={sort('dayOfWeek')}>
                  <Translate contentKey="cpamainApp.accessProfile.dayOfWeek">Day Of Week</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('dayOfWeek')} />
                </th>
                <th className="hand" onClick={sort('action')}>
                  <Translate contentKey="cpamainApp.accessProfile.action">Action</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('action')} />
                </th>
                <th>
                  <Translate contentKey="cpamainApp.accessProfile.lane">Lane</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {accessProfileList.map((accessProfile, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/access-profile/${accessProfile.id}`} color="link" size="sm">
                      {accessProfile.id}
                    </Button>
                  </td>
                  <td>{accessProfile.name}</td>
                  <td>{accessProfile.description}</td>
                  <td>{accessProfile.startTimeOfDay}</td>
                  <td>{accessProfile.endTimeOfDay}</td>
                  <td>{accessProfile.dayOfWeek}</td>
                  <td>
                    <Translate contentKey={`cpamainApp.ActionType.${accessProfile.action}`} />
                  </td>
                  <td>
                    {accessProfile.lanes
                      ? accessProfile.lanes.map((val, j) => (
                          <span key={j}>
                            <Link to={`/lane/${val.id}`}>{val.id}</Link>
                            {j === accessProfile.lanes.length - 1 ? '' : ', '}
                          </span>
                        ))
                      : null}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/access-profile/${accessProfile.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/access-profile/${accessProfile.id}/edit`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() => (window.location.href = `/access-profile/${accessProfile.id}/delete`)}
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
              <Translate contentKey="cpamainApp.accessProfile.home.notFound">No Access Profiles found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default AccessProfile;

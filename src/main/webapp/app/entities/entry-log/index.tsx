import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import EntryLog from './entry-log';
import EntryLogDetail from './entry-log-detail';
import EntryLogUpdate from './entry-log-update';
import EntryLogDeleteDialog from './entry-log-delete-dialog';

const EntryLogRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<EntryLog />} />
    <Route path="new" element={<EntryLogUpdate />} />
    <Route path=":id">
      <Route index element={<EntryLogDetail />} />
      <Route path="edit" element={<EntryLogUpdate />} />
      <Route path="delete" element={<EntryLogDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default EntryLogRoutes;

import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { TextField } from "@mui/material";
import { Select, MenuItem, Pagination } from "@mui/material";
import { Collapse, Alert } from "@mui/material";

function ShiftListPage() {
  const [shifts, setShifts] = useState([]);
  const [pageSize, setPageSize] = useState(25);
  const [searchName, setSearchName] = useState("");
  const [page, setPage] = useState(1);
  const [deleted, setDeleted] = useState(false);
  const [restored, setRestored] = useState(false);

  const fetchShifts = () => {
    setPage(1);
    fetch('/api/v1/shifts')
    .then(response => response.json())
    .then(jsonResponse => setShifts(jsonResponse
      .filter(shift => shift.name.toLowerCase().includes(searchName.toLowerCase()))));
  };

  useEffect(() => {
    fetchShifts();
}, []);

  const handlePageChange = (e, value) => {
    setPage(value);
  };

  const handlePageSizeChange = (e) => {
    setPageSize(e.target.value);
    setPage(1);

  };

  const deleteShift = (shift) => {
    shift.visible = 0;
    fetch('/api/v1/shifts/' + shift.id, {
      method: 'PATCH',
      headers: {
          'Content-Type': 'application/json'
      },
      body: JSON.stringify(shift)
  }).then(fetchShifts);
    setDeleted(true);
    setRestored(false);
  };
  const restoreShift = (shift) => {
    shift.visible = 1;
    fetch('/api/v1/shifts/' + shift.id, {
      method: 'PATCH',
      headers: {
          'Content-Type': 'application/json'
      },
      body: JSON.stringify(shift)
    }).then(fetchShifts);
    setDeleted(false);
    setRestored(true);
  };

  return (
    <div className="mx-3">
      <h2 className="my-5">Pamainos</h2>
      <Collapse in={deleted}>
        <Alert
          onClose={() => {
            setDeleted(false);
          }}
          severity="info"
          className="mb-3"
        >
          Įrašas sėkmingai ištrintas
        </Alert>
      </Collapse>

      <Collapse in={restored}>
        <Alert
          onClose={() => {
            setRestored(false);
          }}
          severity="success"
          className="mb-3"
        >
          Įrašas sėkmingai atstatytas
        </Alert>
      </Collapse>

      <div className="d-flex">
        <button className="btn btn-primary mb-5">
          <Link to="/shifts/create" className="nav-link">
            Pridėti naują pamainą
          </Link>
        </button>
      </div>
      <div className="d-flex justify-content-end">
        <div className="mb-4">
          <form className="d-flex" role="search">
            {/* <label htmlFor="page-size-select" className="me-2">
              Puslapyje:
            </label>
            <Select
              id="page-size-select"
              value={pageSize}
              size="small"
              className="me-2"
              onChange={handlePageSizeChange}
            >
              <MenuItem value={10}>10</MenuItem>
              <MenuItem value={25}>25</MenuItem>
              <MenuItem value={50}>50</MenuItem>
              <MenuItem value={100}>100</MenuItem>
            </Select> */}
            <TextField
              onChange={(e) => setSearchName(e.target.value)}
              value={searchName}
              id="search-name-input"
              label="Ieškoti pavadinimo"
              className="form-control me-2"
              size="small"
            />
            <button
              className="btn btn-outline-primary"
              type="submit"
              onClick={fetchShifts}
            >
              Ieškoti
            </button>
          </form>
        </div>
        {/* <div>
          <Pagination
            count={Math.floor(shifts.length / pageSize) + 1} 
            defaultPage={1}
            siblingCount={0}
            onChange={handlePageChange}
            value={page}
          />
        </div> */}
      </div>

      <table className="table table-hover shadow p-3 mb-5 bg-body rounded align-middle">
        <thead className="table-light">
          <tr>
            <th>Pavadinimas</th>
            <th>Pradžia</th>
            <th>Pabaiga</th>
            <th>Redaguota</th>
            <th>Būsena</th>
            <th className="d-flex justify-content-center">Veiksmai</th>
          </tr>
        </thead>
        <tbody> 
          {shifts.sort((o1,o2) => o2.visible - o1.visible).slice(((page - 1) * pageSize), page * pageSize).map((shift) => (
            <tr
              key={shift.id}
              id={shift.id}
              className={((shift.visible === 1) ? false : true) && "text-black-50"}
            >
              <td>{shift.name}</td>
              <td>{shift.starts}</td>
              <td>{shift.ends}</td>
              <td>{shift.lastUpdated}</td>
              <td>{((shift.visible === 1) ? false : true) ? "Ištrintas" : ""}</td>
              <td className="d-flex justify-content-end">
                <button className="btn btn-outline-primary me-2 my-1" disabled={(shift.visible === 0) ? true : false}>
                  <Link className="nav-link" to={"/shifts/edit/" + shift.id}>
                    Redaguoti
                  </Link>
                </button>
                {((shift.visible === 1) ? false : true) ? (
                  <button
                    className="btn btn-outline-secondary me-2 my-1"
                    onClick={() => restoreShift(shift)}
                  >
                    Atstatyti
                  </button>
                ) : (
                  <button
                    className="btn btn-outline-danger me-2 my-1"
                    onClick={() =>{ deleteShift(shift)}}
                  >
                    Ištrinti
                  </button>
                )}
              </td>
            </tr>
          ))}
        </tbody>
        <tfoot className="table-light">
          <tr>
            <td>
              {(shifts.length === 0)
                ? "Įrašų nerasta"
                : `Rasta įrašų: ${shifts.length}`}
            </td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
          </tr>
        </tfoot>
      </table>
      <div className="d-flex justify-content-end">
        <div className="mb-4">
          <form className="d-flex" role="search">
            <label htmlFor="page-size-select" className="me-2">
              Puslapyje:
            </label>
            <Select
              id="page-size-select"
              value={pageSize}
              size="small"
              className="me-2"
              onChange={handlePageSizeChange}
            >
              <MenuItem value={10}>10</MenuItem>
              <MenuItem value={25}>25</MenuItem>
              <MenuItem value={50}>50</MenuItem>
              <MenuItem value={100}>100</MenuItem>
            </Select>
          </form>
        </div>
        <div>
          <Pagination
            count={Math.floor(shifts.length / pageSize) + 1} 
            defaultPage={1}
            siblingCount={0}
            onChange={handlePageChange}
            value={page}
          />
        </div>
      </div>
    </div>
  );
}

export default ShiftListPage;
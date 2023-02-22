import { Collapse, Alert, Select, MenuItem } from "@mui/material";
import { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import { TextField } from "@mui/material";

function EditShiftPage() {
    const [shift, setShift] = useState({
        name: '',
        starts: '',
        ends: '',
        lastUpdated: ''
    });
  const [numberError, setNumberError] = useState("");
  const [nameError, setNameError] = useState("");
  const [success, setSuccess] = useState(false);
  const [failure, setFailure] = useState(false);
  const [deleted, setDeleted] = useState(false);
  const [restored, setRestored] = useState(false);
  const params = useParams();

  useEffect(() => {
    fetchShift();
  }, [params.id]);

  const fetchShift = () => {
    fetch("/api/v1/shifts/" + params.id)
      .then((response) => response.json())
      .then((jsonResponse) => setShift(jsonResponse));
  }
  const editShift = (e) => {
    e.preventDefault();
    setNumberError(false);
    setNameError(false);

    if (shift.name === "" || shift.starts > shift.ends) {
      if (shift.name === ""){
        setNameError(true);
      }
      if(shift.starts > shift.ends){
        setNumberError(true);
      }
      } else {
        fetch('/api/v1/shifts/' + params.id, {
            method: 'PATCH',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(shift)
        }).then((result) => {
        if (result.ok) {
          setSuccess(true);
          setFailure(false);
          setNumberError(false);
          setNameError(false);
        } else {
          setNameError(true);
          setFailure(true);
          setSuccess(false);
        }
      }).then(fetchShift);
    }
  };
  const updateProperty = (property, event) => {
    setShift({
      ...shift,
      [property]: event.target.value,
    });
  };
  const deleteShift = (shift) => {
    shift.visible = 0;
    fetch('/api/v1/shifts/' + shift.id, {
      method: 'PATCH',
      headers: {
          'Content-Type': 'application/json'
      },
      body: JSON.stringify(shift)
  }).then(fetchShift);
  
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
    }).then(fetchShift);
    setDeleted(false);
    setRestored(true);
  };
  return (
    <div className="mx-3">
      <h2 className="my-5">Redaguoti pamainą</h2>
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
      <Collapse in={success}>
        <Alert
          onClose={() => {
            setSuccess(false);
          }}
          severity="success"
          className="mb-3"
        >
          Įrašas sėkmingai atnaujintas
        </Alert>
      </Collapse>
      <Collapse in={failure}>
        <Alert
          onClose={() => {
            setFailure(false);
          }}
          severity="error"
          className="mb-3"
        >
          Įrašo nepavyko atnaujinti
        </Alert>
      </Collapse>
      <form noValidate>
        <TextField
          error={!!nameError}
          onChange={(e) => updateProperty("name", e)}
          value={shift.name}
          id="create-module-number-with-error"
          label="Pavadinimas"
          helperText="Pavadinimas turi būti unikalus ir negali būti tuščias"
          className="form-control mb-3"
          size="small"
          disabled={shift.visible === 0}
          InputLabelProps={{ shrink: true }}
          required
        />
        <label htmlFor="starts-select" className="me-2">
          Pradžia:
        </label>
        <Select
          id="starts-select"
          error={!!numberError}
          value={shift.starts}
          size="small"
          className="me-2"
          disabled={shift.visible === 0}
          onChange={(e) => updateProperty("starts", e)}
        >
          <MenuItem value="1">1</MenuItem>
          <MenuItem value="2">2</MenuItem>
          <MenuItem value="3">3</MenuItem>
          <MenuItem value="4">4</MenuItem>
          <MenuItem value="5">5</MenuItem>
          <MenuItem value="6">6</MenuItem>
          <MenuItem value="7">7</MenuItem>
          <MenuItem value="8">8</MenuItem>
          <MenuItem value="9">9</MenuItem>
          <MenuItem value="10">10</MenuItem>
          <MenuItem value="11">11</MenuItem>
          <MenuItem value="12">12</MenuItem>
          <MenuItem value="13">13</MenuItem>
          <MenuItem value="14">14</MenuItem>
        </Select>
        <label htmlFor="starts-select" className="me-2">
          Pabaiga:
        </label>
        <Select
          id="ends-select"
          error={!!numberError}
          value={shift.ends}
          size="small"
          className="me-2"
          disabled={shift.visible === 0}
          onChange={(e) => updateProperty("ends", e)}
        >
          <MenuItem value="1">1</MenuItem>
          <MenuItem value="2">2</MenuItem>
          <MenuItem value="3">3</MenuItem>
          <MenuItem value="4">4</MenuItem>
          <MenuItem value="5">5</MenuItem>
          <MenuItem value="6">6</MenuItem>
          <MenuItem value="7">7</MenuItem>
          <MenuItem value="8">8</MenuItem>
          <MenuItem value="9">9</MenuItem>
          <MenuItem value="10">10</MenuItem>
          <MenuItem value="11">11</MenuItem>
          <MenuItem value="12">12</MenuItem>
          <MenuItem value="13">13</MenuItem>
          <MenuItem value="14">14</MenuItem>
        </Select>
        <p>Redaguota: {shift.lastUpdated}</p>
        <p>
          Būsena:{" "}
          {(shift.visible === 1 ? false : true) ? "Pamaina ištrinta" : ""}
        </p>
        <button
          type="submit"
          className="btn btn-primary"
          onClick={editShift}
          disabled={shift.visible === 0}
        >
          Redaguoti
        </button>{" "}
        &nbsp;
        {(shift.visible === 1 ? false : true) ? (
          <button
            className="btn btn-outline-secondary me-2 my-1"
            onClick={() => restoreShift(shift)}
          >
            Atstatyti
          </button>
        ) : (
          <button
            className="btn btn-outline-danger me-2 my-1"
            onClick={() => deleteShift(shift)}
          >
            Ištrinti
          </button>
        )}
      </form>
    </div>
  );
}

export default EditShiftPage;

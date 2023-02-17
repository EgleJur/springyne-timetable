import { useState } from "react";
import { Alert,Collapse } from "@mui/material";
import { TextField } from "@mui/material";
import { Select, MenuItem, Pagination } from "@mui/material";

function CreateShiftPage() {
  const [number, setNumber] = useState("");
  const [name, setName] = useState("");
  const [starts, setStarts] = useState(1);
  const [ends, setEnds] = useState(12);
  const [numberError, setNumberError] = useState("");
  const [nameError, setNameError] = useState("");
  const [success, setSuccess] = useState(false);
  const [failure, setFailure] = useState(false);

  const createNewShift = (e) => {
    e.preventDefault();
    setNumberError(false);
    setNameError(false);
    if (name === "" || starts > ends) {
      if (name === ""){
        setNameError(true);
      }
      if(starts > ends){
        setNumberError(true);
      }
    } else {
    fetch('/api/v1/shifts', {
    method: 'POST',
    headers: {
        'Content-Type': 'application/json'
    },
    body: JSON.stringify(
        {
            name,
            starts,
            ends,
            visible: 1
        }
    ) 
        }).then((result) => {
            if (result.ok) {
              setStarts("1");
              setEnds("12");
              setName("");
              setSuccess(true);
              setFailure(false);
            } else {
              setFailure(true);
              setSuccess(false);
              setNameError(true);
            }
          });
    };
}

  return (
    <div className="mx-3">
      <h2 className="my-5">Pridėti naują pamainą</h2>
      <Collapse in={success}>
        <Alert
          onClose={() => {
            setSuccess(false);
          }}
          severity="success"
          className="mb-3"
        >
          Įrašas sėkmingai sukurtas
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
          Įrašo nepavyko sukurti
        </Alert>
      </Collapse>
      <form noValidate>
        <TextField
          error={!!nameError}
          onChange={(e) => setName(e.target.value)}
          value={name}
          id="create-shift-name"
          label="Pavadinimas"
          helperText="Pavadinimas turi būti unikalus ir negali būti tuščias"
          className="form-control mb-3"
          size="small"
          required
        />
        <label htmlFor="starts-select" className="me-2">
              Pradžia:
            </label>
        <Select
              id="starts-select"
              error={!!numberError}
              value={starts}
              size="small"
              className="me-2"
              onChange={(e) => setStarts(e.target.value)}
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
            </Select>
            <label htmlFor="starts-select" className="me-2">
              Pabaiga:
            </label>
        <Select
              id="ends-select"
              error={!!numberError}
              value={ends}
              size="small"
              className="me-2"
              onChange={(e) => setEnds(e.target.value)}
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
            </Select>
        <button
          type="submit"
          className="btn btn-primary"
          onClick={createNewShift}
        >
          Pridėti
        </button>
      </form>
    </div>
  );
}

export default CreateShiftPage;
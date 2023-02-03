import { Link } from "react-router-dom";

function Navigation() {
    return (
      <div className="m-5">
        <Link to="/">Home</Link>
        &nbsp; | &nbsp;
      </div>
    );
}

export default Navigation;
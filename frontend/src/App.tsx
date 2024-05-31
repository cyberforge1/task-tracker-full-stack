// App.tsx

import React from "react";
import "./App.scss";
import TodoContainer from "./containers/TodoContainer/TodoContainer";

function App() {
  return (
    <div className="container">
      <TodoContainer />
    </div>
  );
}

export default App;
import React from 'react';

const Header = ({ title, lead }) => (
    <div className="rounded px-3 px-sm-4 py-3 py-sm-5 bg-light mb-3">
        <h1 className="display-3">{title}</h1>
        <p className="lead">{lead}</p>
    </div>
)

export default Header;


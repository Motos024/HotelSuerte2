import 'htmx.org';
import 'css/app.css';


/**
 * Register an event at the document for the specified selector,
 * so events are still catched after DOM changes.
 */
function handleEvent(eventType, selector, handler) {
  document.addEventListener(eventType, function(event) {
    if (event.target.matches(selector + ', ' + selector + ' *')) {
      handler.apply(event.target.closest(selector), arguments);
    }
  });
}

handleEvent('click', '.js-dropdown', function(event) {
  document.querySelectorAll('.js-dropdown').forEach(($dropdown) => {
    if (this === $dropdown ||
        ($dropdown.getAttribute('data-dropdown-single') !== 'true' && $dropdown.ariaExpanded === 'true')) {
      $dropdown.ariaExpanded = $dropdown.ariaExpanded !== 'true';
      $dropdown.nextElementSibling.classList.toggle('hidden');
    }
  });
  return false;
});

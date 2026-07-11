#ifndef FILTER_GRAPH_H
#define FILTER_GRAPH_H

#include <string>

namespace vg::subtitle::ffmpeg {

class FilterGraph {
public:
    FilterGraph();
    ~FilterGraph();

    bool setup(const std::string& filterDesc);
};

} // namespace vg::subtitle::ffmpeg

#endif // FILTER_GRAPH_H
